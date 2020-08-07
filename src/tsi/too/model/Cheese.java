package tsi.too.model;

import org.jetbrains.annotations.NotNull;
import tsi.too.ext.StringExt;

import java.math.BigDecimal;
import java.time.LocalDate;

import static tsi.too.Constants.CHEESE;

public class Cheese extends Product {

    @NotNull
    private CheeseType type;

    @NotNull
    private LocalDate manufacturingDate;

    private double quantity = 0;

    public Cheese(
            long code,
            @NotNull CheeseType type,
            @NotNull BigDecimal price,
            @NotNull LocalDate manufacturingDate,
            double quantity
    ) {
        super(code);

        this.type = type;
        this.quantity = quantity;
        setManufacturingDate(manufacturingDate);
        setPrice(price);
        setName(CHEESE);
    }

    public Cheese(
            long code,
            String name,
            MeasureUnity measureUnity,
            @NotNull BigDecimal price,
            int quantity,
            @NotNull CheeseType type,
            @NotNull LocalDate manufacturingDate
    ) {
        super(code, name, measureUnity, price);
        this.quantity = quantity;
        this.type = type;
        setManufacturingDate(manufacturingDate);
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @NotNull
    public CheeseType getType() {
        return type;
    }

    public void setType(@NotNull CheeseType type) {
        this.type = type;
    }

    @NotNull
    public LocalDate getManufacturingDate() {
        return LocalDate.from(manufacturingDate);
    }

    private void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = LocalDate.from(manufacturingDate);
    }

    public double volumeOfMilkNeeded() {
        int LITERS_OF_MILK_PER_KILO_OF_CHEESE = 10;
        return quantity * LITERS_OF_MILK_PER_KILO_OF_CHEESE;
    }

    @NotNull
    public BigDecimal getProductionCost(@NotNull final BigDecimal milkCost) {
        return milkCost.multiply(BigDecimal.valueOf(volumeOfMilkNeeded()))
                .multiply(getPriceForQuantity());
    }

    private BigDecimal getPriceForQuantity() {
        return BigDecimal.valueOf(quantity).multiply(getPrice());
    }

    @Override
    public String toString() {
        return String.format("%s, quantity=%1.1f, type=%s, manufacturingDate=%s}",
                StringExt.removeLastChar(super.toString())
                        .replace(getClass().getSuperclass().getSimpleName(), "Cheese"),
                getQuantity(),
                getType(),
                getManufacturingDate()
        );
    }
}
