package tsi.too.controller;

import org.jetbrains.annotations.NotNull;
import tsi.too.model.Stock;

import java.math.BigDecimal;
import java.time.LocalDate;

import static tsi.too.Constants.INVALID_DATE;
import static tsi.too.Constants.PRICE_MUST_BE_GREATER_THAN_ZERO;
import static tsi.too.util.InputDialog.InputValidator.DEFAULT_SUCCESS_MESSAGE;

public class ProductController {
    private static Long lastCode = 0L;
    protected final Stock stock = Stock.getInstance();

    protected long generateCode() {
        return ++lastCode;
    }

    /**
     * Checks whether a given date is valid. Whereas a date must be within a period of a week.
     *
     * @param date the date to be checked.
     * @return <code>tsi.too.util.InputDialog.InputValidator.DEFAULT_SUCCESS_MESSAGE</code> if is valid,
     * an error message otherwise.
     */
    @NotNull
    protected String validateDate(@NotNull final LocalDate date) {
        var currentDate = LocalDate.now();

        if (date.isAfter(currentDate) || date.isBefore(currentDate.minusWeeks(1)))
            return INVALID_DATE;

        return DEFAULT_SUCCESS_MESSAGE;
    }

    /**
     * Checks if a price is acceptable. A price is acceptable if is greater than zero.
     *
     * @param price the price to be checked.
     * @return <code>DEFAULT_SUCCESS_MESSAGE</code> if valid, an error message otherwise.
     */
    @NotNull
    protected String validatePrice(@NotNull final BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) > 0 ? DEFAULT_SUCCESS_MESSAGE : PRICE_MUST_BE_GREATER_THAN_ZERO;
    }
}
