package tsi.too.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsi.too.ext.StringExt;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;

public abstract class InputDialog {

    /**
     * Shows a dialog requesting input from the user. Loops until the validation rule is satisfied or the user cancels the
     * reading.
     *
     * @param title     the <code>Object</code> to display in the dialog title bar.
     * @param message   the <code>Object</code> to display.
     * @param validator the <code>Object</code> with the validation rule.
     * @return user's input parsed or null if aborted.
     */
    @Nullable
    public static Integer showIntegerInputDialog(String title, String message, @NotNull InputValidator<Integer> validator) {
        int input;
        String s;

        var isValid = false;

        do {
            s = showStringInputDialog(title, message);
            if (s == null)
                return null;

            input = StringExt.toInt(s);
            isValid = validator.isValid(input);

            if (!isValid)
                MessageDialog.showAlertDialog(title, validator.getErrorMessage(input));
        } while (!isValid);

        return input;
    }

    /**
     * Shows a dialog requesting input from the user. Loops until the validation rule is satisfied or the user cancels the
     * reading.
     *
     * @param title     the <code>Object</code> to display in the dialog title bar.
     * @param message   the <code>Object</code> to display.
     * @param validator the <code>Object</code> with the validation rule.
     * @return user's input parsed or null if aborted.
     */
    @Nullable
    public static Long showLongInputDialog(String title, String message, @NotNull InputValidator<Long> validator) {
        long input;
        String s;
        var isValid = false;

        do {
            s = showStringInputDialog(title, message);
            if (s == null)
                return null;

            input = StringExt.toLong(s);
            isValid = validator.isValid(input);

            if (!isValid)
                MessageDialog.showAlertDialog(title, validator.getErrorMessage(input));
        } while (!isValid);

        return input;
    }

    /**
     * Shows a dialog requesting input from the user. Loops until the validation rule is satisfied or the user cancels the
     * reading.
     *
     * @param title     the <code>Object</code> to display in the dialog title bar.
     * @param message   the <code>Object</code> to display.
     * @param validator the <code>Object</code> with the validation rule.
     * @return user's input parsed or null if aborted.
     */
    @Nullable
    public static Double showDoubleInputDialog(
            String title,
            String message,
            @NotNull InputValidator<Double> validator
    ) {
        double input;
        String s;
        var isValid = false;

        do {
            s = showStringInputDialog(title, message);
            if (s == null)
                return null;

            input = StringExt.toDouble(s);
            isValid = validator.isValid(input);

            if (!isValid)
                MessageDialog.showAlertDialog(title, validator.getErrorMessage(input));
        } while (!isValid);

        return input;
    }

    /**
     * Shows a dialog requesting input from the user. Loops until the validation rule is satisfied or the user cancels the
     * reading.
     *
     * @param title     the <code>Object</code> to display in the dialog title bar.
     * @param message   the <code>Object</code> to display.
     * @param validator the <code>Object</code> with the validation rule.
     * @return user's input parsed or null if aborted.
     */
    @Nullable
    public static BigDecimal showBigDecimalInputDialog(String title, String message, @NotNull InputValidator<BigDecimal> validator) {
        BigDecimal input;
        String s;
        var isValid = false;

        do {
            s = showStringInputDialog(title, message);
            if (s == null)
                return null;

            input = StringExt.toBigDecimal(s);
            isValid = validator.isValid(input);

            if (!isValid)
                MessageDialog.showAlertDialog(title, validator.getErrorMessage(input));
        } while (!isValid);

        return input;
    }

    /**
     * Shows a dialog requesting input from the user.
     *
     * @param title   the <code>Object</code> to display in the dialog title bar.
     * @param message the <code>Object</code> to display.
     * @return user's input.
     */
    @Nullable
    public static String showStringInputDialog(String title, String message) {
        return showInputDialog(null, message, title, PLAIN_MESSAGE);
    }

    /**
     * Shows a dialog requesting input from the user.Loops until the validation rule is satisfied or the user cancels the
     * reading.
     *
     * @param title   the <code>Object</code> to display in the dialog title bar.
     * @param message the <code>Object</code> to display.
     * @return user's input.
     */
    @Nullable
    public static String showStringInputDialog(String title, String message, InputValidator<String> validator) {
        String s;
        var isValid = false;

        do {
            s = showStringInputDialog(title, message);
            if (s == null)
                return null;
            isValid = validator.isValid(s);

            if (!isValid)
                MessageDialog.showAlertDialog(title, validator.getErrorMessage(s));
        } while (!isValid);

        return s;
    }

    /**
     * Shows a dialog box asking for user input with the specified <code>mask</code></>. Loops until the validation rule
     * is satisfied or the user cancels the reading.
     *
     * @param title     the <code>Object</code> to display in the dialog title bar.
     * @param message   the <code>Object</code> to display.
     * @param mask      the <code>mask</code> to use.
     * @param validator the <code>Object</code> with validation rule.
     * @return user's input parsed or null if canceled.
     */
    @Nullable
    public static String showMaskedInputDialog(String title, String message, String mask, InputValidator<String> validator) {
        String input;
        var isValid = false;

        do {
            input = InputDialogMasked.showInputDialog(title, message, mask);
            if (input == null)
                return null;

            isValid = validator.isValid(input);

            if (!isValid)
                MessageDialog.showAlertDialog(title, validator.getErrorMessage(input));
        } while (!isValid);

        return input;
    }

    /**
     * Shows a dialog box asking for user input. Loops until the validation rule is satisfied or the user cancels the
     * reading.
     *
     * @param title     the <code>Object</code> to display in the dialog title bar.
     * @param message   the <code>Object</code> to display.
     * @param validator the <code>Object</code> with validation rule.
     * @return user's input parsed or null if canceled.
     */
    @Nullable
    public static LocalDate showBrazilianDateInputDialog(
            String title,
            String message,
            InputValidator<LocalDate> validator
    ) {
        String input;
        String mask = "##/##/####";
        LocalDate result;
        var isValid = false;

        do {
            input = InputDialogMasked.showInputDialog(title, message, mask);
            if (input == null)
                return null;

            try {
                String[] spl = input.split("/");
                result = LocalDate.of(StringExt.toInt(spl[2]), StringExt.toInt(spl[1]), StringExt.toInt(spl[0]));
            } catch (Exception ex) {
                result = LocalDate.MIN;
            }
            isValid = validator.isValid(result);

            if (!isValid)
                MessageDialog.showAlertDialog(title, validator.getErrorMessage(result));
        } while (!isValid);

        return result;
    }

    /**
     * Brings up a choice dialog, where the initial choice is the first one.
     *
     * @param title   the title string for the dialog.
     * @param message the message to display.
     * @param options the available choices.
     * @param <E>     the type of the options.
     * @return user's choice or null if canceled.
     */
    @Nullable
    public static <E> E showOptionDialog(String title, String message, E[] options) {
        int selected = JOptionPane.showOptionDialog(null,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selected != JOptionPane.CLOSED_OPTION)
            return options[selected];

        return null;
    }

    /**
     * A processor that checks an input.
     */
    public interface InputValidator<@NotNull E> {
        String DEFAULT_SUCCESS_MESSAGE = "";

        /**
         * gets the validation result.
         *
         * @param input the <code>Object</code> to be validated.
         * @return the validation result.
         */
        @NotNull String getErrorMessage(E input);

        default boolean isValid(E input) {
            return getErrorMessage(input).equals(DEFAULT_SUCCESS_MESSAGE);
        }
    }
}
