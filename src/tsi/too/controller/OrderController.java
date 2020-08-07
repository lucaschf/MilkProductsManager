package tsi.too.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsi.too.model.CheeseType;
import tsi.too.model.Order;
import tsi.too.model.OrderItem;
import tsi.too.model.Stock;
import tsi.too.util.InputDialog;
import tsi.too.util.MessageDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static tsi.too.Constants.*;
import static tsi.too.ext.NumberExt.toBrazilianCurrency;
import static tsi.too.util.InputDialog.InputValidator.DEFAULT_SUCCESS_MESSAGE;

public class OrderController {
    private static int lastOrderNumber = 0;
    private static ArrayList<Order> orderHistoric = new ArrayList<>();
    private static OrderController instance;
    private CheeseController cheeseController = CheeseController.getInstance();
    private Stock stock = Stock.getInstance();

    public static List<Order> getOrderHistoric() {
        return orderHistoric;
    }

    public static boolean isOrderHistoricEmpty() {
        return orderHistoric.isEmpty();
    }

    public static OrderController getInstance() {
        synchronized (OrderController.class) {
            if (instance == null) {
                instance = new OrderController();
            }

            return instance;
        }
    }

    private long generateOrderNumber() {
        return ++lastOrderNumber;
    }

    public void placeOrder() {
        if (stock.size() == 0) {
            MessageDialog.showAlertDialog(NEW_SALE, NO_ITEMS_FOR_SALE);
            return;
        }

        Order order = readOrderData();
        if (order == null) return;

        orderHistoric.add(order);
        writeOfStock(order.getItems());

        MessageDialog.showInformationDialog(
                NEW_SALE,
                String.format("%s: %d %s", ORDER, order.getNumber(), SUCCESSFUL_PLACED)
        );
    }

    @Nullable
    private Order readOrderData() {
        var customerName = InputDialog.showStringInputDialog(
                NEW_SALE,
                CUSTOMER_NAME,
                input -> input.isEmpty() ? CUSTOMER_NAME_MUST_BE_INFORMED : DEFAULT_SUCCESS_MESSAGE
        );
        if (customerName == null) return null;

        var items = readItems();
        if (items.isEmpty()) return null;

        return new Order(generateOrderNumber(), customerName, items);
    }

    @NotNull
    private List<OrderItem> readItems() {
        CheeseType type;
        var items = new ArrayList<OrderItem>();

        HashMap<CheeseType, Double> stock = new HashMap<>();

        do {
            final double available;
            type = InputDialog.showOptionDialog(NEW_SALE, TYPES_OF_CHEESE, CheeseType.values());

            if (type != null) {
                if (!stock.containsKey(type)) {
                    available = cheeseController.getStockCount(type);
                    stock.put(type, available);
                } else
                    available = stock.get(type);

                var quantity = InputDialog.showDoubleInputDialog(
                        NEW_SALE,
                        HOW_MANY,
                        input -> validateItemQuantity(available, input)
                );

                if (quantity == null) // user canceled
                    return items;

                stock.put(type, available - quantity);
                items.add(new OrderItem(type.toString(), quantity, cheeseController.getLastCheesePrice(type)));
            }
        } while (type != null && MessageDialog.showConfirmationDialog(NEW_SALE, ADD_MORE_ITEMS));

        return items;
    }

    private String validateItemQuantity(double available, final Double input) {
        return input == 0 ? INVALID_QUANTITY : input > available ?
                String.format("%s: %1.2f", AVAILABLE_QUANTITY, available) : "";
    }

    private void writeOfStock(@NotNull final List<OrderItem> items) {
        items.forEach(item -> cheeseController.writeOffInventory(item.getQuantity()));
    }

    public void showOrdersReport() {
        String message;

        if (!OrderController.isOrderHistoricEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Order order : OrderController.getOrderHistoric()) {
                sb.append(buildOrderReportEntry(order));
            }
            message = sb.toString();
        } else
            message = NO_DATA_FOUND;

        MessageDialog.showTextMessage(ORDERS_REPORT, message);
    }

    @NotNull
    private String buildOrderReportEntry(@NotNull final Order order) {
        return String.format("%s%s%s%s%s%s\n",
                String.format("%s: %d", ORDER_NUMBER, order.getNumber()),
                String.format("\n%s: %s", CUSTOMER_NAME, order.getCustomer()),
                String.format("\n%s: %s", PLACEMENT_DATA, order.getPlacementDate()),
                String.format("\n%s: %s", ITEMS, buildItemsMessage(order)),
                String.format("\n\n%s: %s", TOTAL, toBrazilianCurrency(order.getTotalCost())),
                REPORT_ITEM_SEPARATOR
        );
    }

    @NotNull
    private String buildItemsMessage(@NotNull final Order order) {
        var sb = new StringBuilder();

        order.getItems().forEach(item ->
                sb.append(String.format("\n\n  %s: %s", DESCRIPTION, item.getDescription()))
                        .append(String.format("\n  %s: %1.2f", QUANTITY, item.getQuantity()))
                        .append(String.format("\n  %s: %s", UNITARY_COST, toBrazilianCurrency(item.getUnitaryCost())))
                        .append(String.format("\n  %s: %s", TOTAL, toBrazilianCurrency(item.getPriceForQuantity())))
        );

        return sb.toString();
    }
}