package tsi.too.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsi.too.model.Milk;
import tsi.too.model.MilkPriceEntry;
import tsi.too.model.MilkType;
import tsi.too.model.Product;
import tsi.too.util.InputDialog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static tsi.too.Constants.*;
import static tsi.too.util.InputDialog.InputValidator.DEFAULT_SUCCESS_MESSAGE;

public class MilkController extends ProductController {
    private static MilkController instance;
    private static ArrayList<MilkPriceEntry> milkPriceChanges = new ArrayList<>();

    /**
     * Ensures that only one instance of this class is created.
     *
     * @return an instance of this class.
     */
    public static MilkController getInstance() {
        synchronized (MilkController.class) {
            if (instance == null) {
                instance = new MilkController();
            }

            return instance;
        }
    }

    public void registerMilk() {
        var milk = readMilkData();

        if (milk == null)
            return;

        addPriceEntry(new MilkPriceEntry(milk.getDeliveryDate(), milk.getPrice()));
        stock.insert(milk);
    }

    @Nullable
    private Milk readMilkData() {
        var type = InputDialog.showOptionDialog(REGISTER_MILK, TYPES_OF_MILK, MilkType.values());
        if (type == null) return null;

        var volume = InputDialog.showDoubleInputDialog(
                REGISTER_MILK,
                RECEIVED_VOLUME_IN_LITERS,
                input -> input > 0 ? DEFAULT_SUCCESS_MESSAGE : VOLUME_MUST_BE_GREATER_THAN_ZERO);
        if (volume == null) return null;

        var deliveryDate = InputDialog.showBrazilianDateInputDialog(
                REGISTER_MILK,
                RECEIPT_DATE,
                this::validateDate
        );

        if (deliveryDate == null) return null;

        var price = InputDialog.showBigDecimalInputDialog(
                REGISTER_MILK,
                LITER_COST,
                this::validatePrice
        );
        if (price == null) return null;

        return new Milk(generateCode(), type, price, volume, deliveryDate);
    }

    /**
     * Gets the total volume of milk stored at the specified <code>data</code>.
     * Will return 0 if the milk stored on the <code>date</code> has already been released from stock
     *
     * @return the volume.
     */
    public double totalMilkVolume(final LocalDate date) {
        if (date == null)
            return 0;

        return stock.getItems().stream().filter(product -> product instanceof Milk &&
                ((Milk) product).getDeliveryDate().equals(date))
                .map(Milk.class::cast)
                .mapToDouble(Milk::getVolume).sum();
    }

    /**
     * Gets the total milk volume in the stock.
     *
     * @return the volume.
     */
    public double totalMilkVolume() {
        return stock.getItems().stream().filter(product -> product instanceof Milk)
                .map(Milk.class::cast)
                .mapToDouble(Milk::getVolume).sum();
    }

    /**
     * Adds an milk price on the milk prices history.
     *
     * @param entry the price data.
     */
    private void addPriceEntry(@NotNull final MilkPriceEntry entry) {
        var target = milkPriceChanges.stream().filter(entry1 -> entry1.equals(entry)).findFirst().orElse(null);
        if (target == null)
            milkPriceChanges.add(entry);
    }

    /**
     * Writes off a <code>volume</code> of milk from stock.
     *
     * @param volume the amount to be written off.
     * @param date   the date of milk storage.
     */
    public void writeOffInventory(double volume, final LocalDate date) {
        if (volume <= 0 || date == null)
            return;

        double removed = 0;
        List<Product> items = stock.getItems(product -> product instanceof Milk &&
                ((Milk) product).getDeliveryDate().equals(date));

        for (Product m : items) {
            Milk item = (Milk) m;

            var needRemove = volume - removed;

            if (item.getVolume() == needRemove) {
                stock.remove(m);
                return;
            }

            if (item.getVolume() > needRemove) {
                stock.remove(m);

                needRemove = 0;
                item.setVolume(item.getVolume() - needRemove);

                stock.insert(item);
                return;
            }

            removed += item.getVolume();
            stock.remove(m);
        }
    }

    /**
     * Gets the updated milk price based on a date.
     *
     * @param date the date we looking for.
     * @return the last price or zero if has not that <code>type</code> in stock.
     */
    public BigDecimal getLastMilkPrice(final LocalDate date) {
        if (milkPriceChanges.isEmpty())
            return BigDecimal.ZERO;

        var priceEntry = milkPriceChanges.stream()
                .filter(entry -> entry.getDate().equals(date))
                .min(Comparator.comparing(MilkPriceEntry::getDate))
                .orElse(null);

        return priceEntry != null ? priceEntry.getPrice() : BigDecimal.ZERO;
    }
}