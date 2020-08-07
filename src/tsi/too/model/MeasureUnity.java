package tsi.too.model;

import org.jetbrains.annotations.NotNull;
import tsi.too.Constants;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MeasureUnity {
    LITER(Constants.LITER, Constants.LITER_ABBREV),
    KILO(Constants.KILO, Constants.KILO_ABBREV);

    private static Map<Integer, MeasureUnity> mappedValues;
    private String description;
    private String abbreviation;

    MeasureUnity(String description, String abbreviation) {
        this.description = description;
        this.abbreviation = abbreviation;
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
    public static MeasureUnity from(int ordinal) throws IllegalArgumentException {
        mapValues();

        var measureUnity = mappedValues.get(ordinal);

        if (measureUnity == null)
            throw new IllegalArgumentException("No such measure unity");

        return measureUnity;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}