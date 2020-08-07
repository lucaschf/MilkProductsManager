package tsi.too.model;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public abstract class Product {
    private long code;
    private String name;
    private MeasureUnity measureUnity;

    @NotNull
    private BigDecimal price = BigDecimal.ZERO;

    public Product(long code) {
        this.code = code;
        setName("");
        setMeasureUnity(MeasureUnity.KILO);
    }

    public Product(
            long code,
            String name,
            MeasureUnity measureUnity,
            @NotNull BigDecimal price
    ) {
        this.code = code;
        setName(name);
        setMeasureUnity(measureUnity);
        setPrice(price);
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public BigDecimal getPrice() {
        return new BigDecimal(price.toString());
    }

    public void setPrice(@NotNull BigDecimal price) {
        this.price = new BigDecimal(price.toString());
    }

    public MeasureUnity getMeasureUnity() {
        return measureUnity;
    }

    public void setMeasureUnity(MeasureUnity measureUnity) {
        this.measureUnity = measureUnity;
    }

    @Override
    public String toString() {
        return String.format(
                "Product{code= %d name='%s', measureUnity=%s, price=%1.2f}",
                getCode(),
                getName(),
                getMeasureUnity(),
                getPrice()
        );
    }
}