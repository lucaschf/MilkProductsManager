package tsi.too.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final long number;
    private String customer;
    private LocalDateTime placementDate;
    private ArrayList<OrderItem> items = new ArrayList<>();

    public Order(
            long number,
            String customer,
            LocalDateTime placementDate,
            List<OrderItem> items
    ) {
        this.number = number;
        setCustomer(customer);
        setPlacementDate(placementDate);

        addItems(items);
    }

    public Order(
            long number,
            String customer,
            List<OrderItem> items
    ) {
        this.number = number;
        setCustomer(customer);
        setPlacementDate(LocalDateTime.now());

        addItems(items);
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public LocalDateTime getPlacementDate() {
        return LocalDateTime.from(placementDate);
    }

    public void setPlacementDate(LocalDateTime placementDate) {
        this.placementDate = LocalDateTime.from(placementDate);
    }

    public long getNumber() {
        return number;
    }

    public void addItem(@NotNull OrderItem item) {
        var targetItem = getItem(item.getDescription());

        if (targetItem == null) {
            items.add(item);
            return;
        }

        targetItem.increaseQuantity(item.getQuantity()); // Avoids inserting equals items twice.
    }

    public void addItems(@NotNull List<OrderItem> items) {
        items.forEach(this::addItem);
    }

    @Nullable
    private OrderItem getItem(String description) {
        return items.stream()
                .filter(product -> product.getDescription().equals(description))
                .findFirst().orElse(null);
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    @NotNull
    public BigDecimal getTotalCost() {
        BigDecimal cost = BigDecimal.ZERO;

        for (OrderItem item : items) {
            cost = cost.add(item.getPriceForQuantity());
        }

        return cost;
    }

    @Override
    public String toString() {
        return String.format(
                "Order{number=%d, customer='%s', placementDate=%s, items=%s}",
                number,
                customer,
                placementDate,
                items
        );
    }
}
