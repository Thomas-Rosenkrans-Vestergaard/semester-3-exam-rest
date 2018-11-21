package com.group3.sem3exam.logic.validation;

import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;

public class IsAfterCheck<R> implements ResourceValidatorAttributeCheck<R, Temporal>
{

    /**
     * The attribute value to perform validation upon.
     */
    private final Temporal attributeValue;

    /**
     * The name of the attribute to perform validation upon.
     */
    private final String attributeName;

    /**
     * The cutoff of the temporal comparison.
     * <p>
     * For the check to pass the attribute value must be greater than the cutoff.
     */
    private final Temporal cutoff;

    /**
     * Creates a new {@link IsAfterCheck}.
     *
     * @param attributeValue The attribute value to perform validation upon.
     * @param attributeName  The name of the attribute to perform validation upon.
     * @param cutoff         The cutoff of the temporal comparison.
     */
    public IsAfterCheck(Temporal attributeValue, String attributeName, Temporal cutoff)
    {
        this.attributeValue = attributeValue;
        this.attributeName = attributeName;
        this.cutoff = cutoff;
    }

    /**
     * Creates a new constructor for the {@link IsAfterCheck}.
     *
     * @param cutoff The cutoff of the validation operation.
     * @param <R2>   The type of the parent resource.
     * @return The constructor.
     */
    public static <R2> ResourceValidatorAttributeCheckConstructor<R2, Temporal> constructor(Temporal cutoff)
    {
        return (parentValue, attributeValue, attributeName) -> new IsAfterCheck<>(attributeValue, attributeName, cutoff);
    }

    /**
     * Checks if the {@link Temporal} attributeValue is after the provided cutoff.
     *
     * @return The result of the condition evaluation.
     */
    @Override
    public boolean passes()
    {
        Comparable cutoffComparable    = (Comparable) cutoff;
        Comparable attributeComparable = (Comparable) attributeValue;

        return attributeComparable.compareTo(cutoffComparable) > 0;
    }

    /**
     * Creates a new {@link ValidationViolation} based on the provided attributeValue and attributeName.
     *
     * @return The newly created {@link ValidationViolation}.
     */
    @Override
    public ValidationViolation createViolation()
    {
        Map<String, String> map = new HashMap<>();
        map.put("cutoff", this.cutoff.toString());

        return new ValidationViolation(
                String.format("Provided %s is not after %s.", attributeName, cutoff.toString()),
                "IsAfterCheck",
                this.attributeName,
                attributeValue,
                map
        );
    }
}
