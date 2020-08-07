package tsi.too.model;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class OrderItem {
    private String description;
    private double quantity;

    @NotNull
    private BigDecimal unitaryCost = BigDecimal.ZERO;

    public OrderItem(String description, double quantity, @NotNull BigDecimal unitaryCost) {
        setDescription(description);
        setQuantity(quantity);
        setUnitaryCost(unitaryCost);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitaryCost() {
        return new BigDecimal(unitaryCost.toString());
    }

    public void setUnitaryCost(@NotNull final BigDecimal unitaryCost) {
        this.unitaryCost = new BigDecimal(unitaryCost.toString());
    }

    public void increaseQuantity(double amount) {
        setQuantity(getQuantity() + amount);
    }

    @NotNull
    public BigDecimal getPriceForQuantity() {
        return unitaryCost.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return String.format(
                "OrderItem{description='%s', quantity=%s, unitaryCost=%s}",
                description,
                quantity,
                unitaryCost
        );
    }
}
