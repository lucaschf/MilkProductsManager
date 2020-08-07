package tsi.too.controller;

import org.jetbrains.annotations.NotNull;
import tsi.too.util.InputDialog;
import tsi.too.util.MessageDialog;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static tsi.too.Constants.*;
import static tsi.too.util.InputDialog.InputValidator.DEFAULT_SUCCESS_MESSAGE;

public class MenuController {
    private static MenuController instance;
    private final List<String> options = Arrays.asList(
            REGISTER_MILK,
            REGISTER_CHEESE,
            PLACE_ORDER,
            GENERAL_PRODUCTION_REPORT,
            DAILY_PRODUCTION_REPORT,
            ORDERS_REPORT,
            EXIT
    );
    private MilkController milkController = MilkController.getInstance();
    private CheeseController cheeseController = CheeseController.getInstance();
    private OrderController orderController = OrderController.getInstance();

    /**
     * Ensures that only one instance of this class is created.
     *
     * @return an instance of this class.
     */
    public static MenuController getInstance() {
        synchronized (MenuController.class) {
            if (instance == null)
                instance = new MenuController();

            return instance;
        }
    }

    public void menu() {
        String selected;

        do {
            selected = InputDialog.showOptionDialog(
                    MILK_PRODUCTS_MANAGER,
                    "",
                    options.toArray(String[]::new)
            );

            if (selected == null)
                System.exit(0);

            execute(selected);
        } while (!selected.equals(EXIT));
    }

    private void execute(@NotNull final String selected) {
        switch (selected) {
            case REGISTER_MILK:
                milkController.registerMilk();
                break;
            case REGISTER_CHEESE:
                cheeseController.registerCheese();
                break;
            case PLACE_ORDER:
                orderController.placeOrder();
                break;
            case GENERAL_PRODUCTION_REPORT:
                cheeseController.showProductionReport();
                break;
            case DAILY_PRODUCTION_REPORT:
                showProductionReport();
                break;
            case ORDERS_REPORT:
                orderController.showOrdersReport();
                break;
        }
    }

    private void showProductionReport() {
        if (CheeseController.getHistoricSize() == 0) {
            MessageDialog.showTextMessage(DAILY_PRODUCTION_REPORT, NO_DATA_FOUND);
            return;
        }

        LocalDate date = InputDialog.showBrazilianDateInputDialog(
                DAILY_PRODUCTION_REPORT,
                MANUFACTURING_DATE,
                input -> !input.isAfter(LocalDate.now()) ? DEFAULT_SUCCESS_MESSAGE : INVALID_DATE
        );

        if (date != null)
            cheeseController.showProductionReport(date);
    }
}
