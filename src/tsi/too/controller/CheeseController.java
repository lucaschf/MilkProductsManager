package tsi.too.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsi.too.model.Cheese;
import tsi.too.model.CheeseType;
import tsi.too.model.MeasureUnity;
import tsi.too.util.InputDialog;
import tsi.too.util.MessageDialog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static tsi.too.Constants.*;
import static tsi.too.ext.NumberExt.toBrazilianCurrency;
import static tsi.too.util.InputDialog.InputValidator.DEFAULT_SUCCESS_MESSAGE;

public class CheeseController extends ProductController {
    public static final int LITERS_OF_MILK_PER_KILO_OF_CHEESE = 10;
    private static CheeseController instance;
    private static ArrayList<Cheese> productionHistory = new ArrayList<>();

    private MilkController milkController = MilkController.getInstance();

    private CheeseController() {
    }

    @NotNull
    public static List<Cheese> getProductionHistory() {
        return productionHistory;
    }

    public static double getHistoricSize() {
        return productionHistory.size();
    }

    /**
     * Ensures that only one instance of this class is created.
     *
     * @return an instance of this class.
     */
    public static CheeseController getInstance() {
        synchronized (CheeseController.class) {
            if (instance == null) {
                instance = new CheeseController();
            }

            return instance;
        }
    }

    public void registerCheese() {
        if (milkController.totalMilkVolume() == 0) {
            MessageDialog.showAlertDialog(REGISTER_CHEESE, THERE_IS_NO_MILK_IN_STOCK);
            return;
        }

        var cheese = readCheeseData();

        if (cheese == null)
            return;

        stock.insert(cheese);
        productionHistory.add(cheese);

        milkController.writeOffInventory(getAmountOfMilkNeeded(cheese.getQuantity()),
                cheese.getManufacturingDate());
    }

    @Nullable
    private Cheese readCheeseData() {
        var type = InputDialog.showOptionDialog(REGISTER_CHEESE, TYPES_OF_CHEESE, CheeseType.values());
        if (type == null) return null;

        var manufacturingDate = InputDialog.showBrazilianDateInputDialog(
                REGISTER_CHEESE,
                MANUFACTURING_DATE,
                this::validateDate
        );
        if (manufacturingDate == null) return null;

        var quantity = InputDialog.showDoubleInputDialog(
                REGISTER_CHEESE,
                QUANTITY_PRODUCED,
                input -> canBeProduced(input, manufacturingDate)
        );
        if (quantity == null) return null;

        BigDecimal price = InputDialog.showBigDecimalInputDialog(
                REGISTER_CHEESE,
                UNITARY_COST,
                this::validatePrice
        );
        if (price == null) return null;

        return new Cheese(generateCode(), type, price, manufacturingDate, quantity);
    }

    /**
     * Checks whether a date is valid for making cheese. A date is considered valid if it is in the last week and you have
     * milk in stock on the chosen day.
     *
     * @param date the date to be checked.
     * @return <code>DEFAULT_SUCCESS_MESSAGE</code> if valid, an error message otherwise.
     */
    @Override
    protected @NotNull String validateDate(@NotNull final LocalDate date) {
        var errorMessage = super.validateDate(date);

        if (errorMessage.equals(DEFAULT_SUCCESS_MESSAGE) && milkController.totalMilkVolume(date) == 0)
            errorMessage = THERE_IS_NO_MILK_IN_STOCK_FOR_THIS_DATE;

        return errorMessage;
    }

    private double getAmountOfMilkNeeded(double quantity) {
        return quantity * LITERS_OF_MILK_PER_KILO_OF_CHEESE;
    }

    /**
     * Checks whether a certain <code>quantity</code> of cheese can be produced in a given <code>manufacturingDate</code>.
     * A cheese can be produced if, on the required date, there is enough milk in stock to make the requested <code>quantity</code>
     *
     * @param quantity          the quantity to be produced
     * @param manufacturingDate the date which the cheese will be produced
     * @return <code>DEFAULT_SUCCESS_MESSAGE</code> if valid, an error message otherwise.
     */
    @NotNull
    private String canBeProduced(double quantity, @NotNull final LocalDate manufacturingDate) {
        var totalMilkAvailable = milkController.totalMilkVolume(manufacturingDate);

        if (totalMilkAvailable == 0)
            return THERE_IS_NO_MILK_IN_STOCK_FOR_THIS_DATE;

        if (getAmountOfMilkNeeded(quantity) > totalMilkAvailable)
            return INSUFFICIENT_MILK_STOCK;

        return DEFAULT_SUCCESS_MESSAGE;
    }

    /**
     * Gets the amount of cheese in stock based on its <code>type</code>.
     *
     * @param type the type we looking for.
     * @return the quantity in stock.
     */
    public double getStockCount(CheeseType type) {
        return stock.getItems(product -> product instanceof Cheese &&
                ((Cheese) product).getType() == type)
                .stream().map(Cheese.class::cast)
                .mapToDouble(Cheese::getQuantity)
                .sum();
    }

    /**
     * Gets the updated cheese price based on its <code>type</code>.
     *
     * @param type the type we looking for.
     * @return the last price or zero if has not that <code>type</code> in stock.
     */
    public BigDecimal getLastCheesePrice(CheeseType type) {
        var cheese = stock.getItems(product -> product instanceof Cheese &&
                ((Cheese) product).getType() == type)
                .stream().map(Cheese.class::cast)
                .min(Comparator.comparing(Cheese::getManufacturingDate))
                .orElse(null);

        return cheese != null ? cheese.getPrice() : BigDecimal.ZERO;
    }

    //region Report

    public void writeOffInventory(double quantity) {
        if (quantity <= 0)
            return;

        double removed = 0;
        var items = stock.getItems(product -> product instanceof Cheese).stream()
                .map(Cheese.class::cast)
                .sorted(Comparator.comparing(Cheese::getManufacturingDate))
                .collect(Collectors.toList());

        for (Cheese item : items) {
            var needRemove = quantity - removed;

            if (item.getQuantity() == needRemove) {
                stock.remove(item);
                return;
            }

            if (item.getQuantity() > needRemove) {
                stock.remove(item);

                needRemove = 0;
                item.setQuantity(item.getQuantity() - needRemove);

                stock.insert(item);
                return;
            }

            removed += item.getQuantity();
            stock.remove(item);
        }
    }

    /**
     * Displays a general production report.
     */
    public void showProductionReport() {
        showProductionReport(GENERAL_PRODUCTION_REPORT, CheeseController.getProductionHistory());
    }

    /**
     * Displays a production report based on the specified <code>date</code>
     *
     * @param date the date we looking for.
     */
    public void showProductionReport(final LocalDate date) {
        var items = CheeseController.getProductionHistory().stream()
                .filter(item -> item.getManufacturingDate().equals(date))
                .collect(Collectors.toList());

        showProductionReport(DAILY_PRODUCTION_REPORT, items);
    }

    /**
     * Builds and displays the production report based on given <code>products</code>.
     *
     * @param title    the report's title
     * @param products the report's info
     */
    private void showProductionReport(@NotNull String title, @NotNull final List<Cheese> products) {
        String message;

        if (products.isEmpty())
            message = NO_DATA_FOUND;
        else {
            StringBuilder sb = new StringBuilder();
            for (Cheese cheese : CheeseController.getProductionHistory()) {
                sb.append(buildCheeseReportEntry(cheese));
            }
            message = sb.toString();
        }

        MessageDialog.showTextMessage(title, message);
    }
    //endregion

    /**
     * Builds an entry for the production report.
     *
     * @param cheese the source to the entry.
     * @return an entry message.
     */
    @NotNull
    private String buildCheeseReportEntry(@NotNull final Cheese cheese) {

        return String.format("%s%s%s%s%s\n",
                String.format("%s: %s", ITEM, cheese.getName()),
                String.format("\n%s: %s", TYPE, cheese.getType()),
                String.format("\n%s: %1.2f%s", VOLUME_OF_MILK_USED, cheese.volumeOfMilkNeeded(), MeasureUnity.LITER.getAbbreviation()),
                String.format("\n%s: %s", PRODUCTION_COST,
                        toBrazilianCurrency(
                                cheese.getProductionCost(milkController.getLastMilkPrice(cheese.getManufacturingDate()))
                        )
                ),
                REPORT_ITEM_SEPARATOR
        );
    }
}