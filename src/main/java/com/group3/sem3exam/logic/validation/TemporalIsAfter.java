package com.group3.sem3exam.logic.validation;

import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TemporalIsAfter<R> extends AbstractResourceValidatorCondition<R, Temporal>
{

    /**
     * The name of the attribute being validated.
     */
    private final String attributeName;

    /**
     * The cutoff of the {@link TemporalIsAfter} check.
     */
    private final Temporal cutoff;

    /**
     * Creates a new {@link TemporalIsAfter} check.
     *
     * @param attributeName The name of the attribute being validated.
     * @param factory       The factory that returns the value to be validated.
     * @param cutoff        The cutoff of the object being tested.
     */
    public TemporalIsAfter(String attributeName, Function<R, Temporal> factory, Temporal cutoff)
    {
        super(factory);
        this.attributeName = attributeName;
        this.cutoff = cutoff;
    }

    /**
     * Checks if the provided {@link Temporal} is after the provided cutoff.
     *
     * @param object The object to perform the validation condition upon.
     * @return The result of the condition evaluation.
     */
    @Override
    public boolean isTrue(Temporal object)
    {
        Comparable cutoffComparable = (Comparable) cutoff;
        Comparable objectComparable = (Comparable) object;

        return objectComparable.compareTo(cutoffComparable) > 0;
    }

    /**
     * Creates a new {@link ValidationViolation} for the provided object.
     *
     * @param object The object from which to create the {@link ValidationViolation}.
     * @return The newly created {@link ValidationViolation}.
     */
    @Override
    public ValidationViolation createViolation(Temporal object)
    {
        Map<String, String> map = new HashMap<>();
        map.put("cutoff", this.cutoff.toString());

        return new ValidationViolation(
                String.format("Provided %s is not after %s."),
                "IsAfterCheck",
                this.attributeName,
                object.toString(),
                map
        );
    }
}
