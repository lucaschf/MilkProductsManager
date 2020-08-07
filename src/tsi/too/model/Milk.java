package tsi.too.model;

import org.jetbrains.annotations.NotNull;
import tsi.too.Constants;
import tsi.too.ext.StringExt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Milk extends Product {
    @NotNull
    private MilkType type = MilkType.FRESH;
    @NotNull
    private LocalDate deliveryDate = LocalDate.now();
    private double volume = 0;

    public Milk(long code) {
        super(code);
        setMeasureUnity(MeasureUnity.LITER);
        setName(Constants.MILK);
    }

    public Milk(
            long code,
            String name,
            MeasureUnity measureUnity,
            @NotNull BigDecimal price,
            @NotNull MilkType type,
            double volume,
            @NotNull LocalDate deliveryDate
    ) {
        super(code, name, measureUnity, price);
        setMeasureUnity(MeasureUnity.LITER);
        setType(type);
        setVolume(volume);
        setDeliveryDate(deliveryDate);
    }

    public Milk(
            long code,
            @NotNull MilkType type,
            @NotNull BigDecimal price,
            double volume,
            @NotNull LocalDate deliveryDate
    ) {
        super(code);
        setType(type);
        setVolume(volume);
        setDeliveryDate(deliveryDate);
        setPrice(price);
        setMeasureUnity(MeasureUnity.LITER);
        setName(Constants.MILK);
    }

    @NotNull
    public MilkType getType() {
        return type;
    }

    public void setType(@NotNull MilkType type) {
        this.type = type;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @NotNull
    public LocalDate getDeliveryDate() {
        return LocalDate.from(deliveryDate);
    }

    public void setDeliveryDate(@NotNull final LocalDate deliveryDate) {
        this.deliveryDate = LocalDate.from(deliveryDate);
    }

    @Override
    public String toString() {
        return String.format(
                "%s, type=%s, volume=%s, receiveAt=%s}",
                StringExt.removeLastChar(super.toString())
                        .replace(getClass().getSuperclass().getSimpleName(), "Milk"),
                type,
                volume,
                deliveryDate
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Milk milk = (Milk) o;

        return Double.compare(milk.getVolume(), getVolume()) == 0 &&
                getType() == milk.getType() &&
                getDeliveryDate().equals(milk.getDeliveryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getDeliveryDate(), getVolume());
    }
}