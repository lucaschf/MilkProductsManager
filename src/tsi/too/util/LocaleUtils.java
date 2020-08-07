package tsi.too.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class LocaleUtils {

    @NotNull
    @Contract(" -> new")
    public static Locale getBrazilianLocale() {
        return new Locale("pt", "BR");
    }
}
