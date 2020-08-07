package tsi.too.model;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MilkPriceEntry {
    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal price;

    public MilkPriceEntry(@NotNull LocalDate date, @NotNull BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public LocalDate getDate() {
        return LocalDate.from(date);
    }

    public BigDecimal getPrice() {
        return new BigDecimal(price.toString());
    }

    @Override
    public String toString() {
        return String.format("MilkPriceEntry{date=%s, price=%s}", date, price);
    }
}