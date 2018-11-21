package com.group3.sem3exam.logic.validation;

import java.util.function.Function;

public interface ResourceValidatorCondition<R, V>
{

    /**
     * Checks if the object conforms to the condition.
     *
     * @param object The object to perform the validation condition upon.
     * @return The result of the condition evaluation.
     */
    boolean isTrue(V object);

    /**
     * Creates a new {@link ValidationViolation} for the provided object.
     *
     * @param object The object from which to create the {@link ValidationViolation}.
     * @return The newly created {@link ValidationViolation}.
     */
    ValidationViolation createViolation(V object);

    /**
     * Returns the factory that returns the value V from the object R.
     *
     * @return The factory that returns the value V from the object R.
     */
    Function<R, V> getFactory();
}
