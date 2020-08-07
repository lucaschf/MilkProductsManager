package tsi.too.ext;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

import static tsi.too.util.LocaleUtils.getBrazilianLocale;

public abstract class NumberExt {

    @NotNull
    public static String toBrazilianCurrency(@NotNull final Number number) {
        return NumberFormat.getCurrencyInstance(getBrazilianLocale()).format(number);
    }
}