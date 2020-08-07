package tsi.too.model;

import org.jetbrains.annotations.NotNull;
import tsi.too.Constants;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CheeseType {

    MOZZARELLA(Constants.MOZZARELLA),
    STANDARD_MINES(Constants.STANDARD_MINES),
    PARMESAN(Constants.PARMESAN),
    DISH(Constants.DISH),
    FRESCO(Constants.FRESCO);

    private static Map<Integer, CheeseType> mappedValues;

    private String description;

    CheeseType(@NotNull String description) {
        this.description = description;
    }

    /**
     * Convenience method for retrieving an type based on its ordinal.
     *
     * @param ordinal the ordinal to be searched.
     * @return the equivalent type
     * @throws IllegalArgumentException if the ordinal is not contained in the values.
     */
    @SuppressWarnings("unused")
    @NotNull
    public static CheeseType from(int ordinal) throws IllegalArgumentException {
        mapValues();

        CheeseType type = mappedValues.get(ordinal);

        if (type != null)
            return type;

        throw new IllegalArgumentException("No such type");
    }

    /**
     * Maps the <code>values</code> into a hashMap to make easier to retrieve a value based on its ordinal.
     */
    private static void mapValues() {
        if (mappedValues == null)
            mappedValues = Arrays.stream(values()).collect(Collectors.toMap(Enum::ordinal, Function.identity()));
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}