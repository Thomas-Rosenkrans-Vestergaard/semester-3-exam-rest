package com.group3.sem3exam.logic.validation;

public interface ResourceValidatorAttributeCheck<R, V>
{

    /**
     * Checks if the object conforms to the condition.
     *
     * @return The result of the condition evaluation.
     */
    boolean passes();

    /**
     * Creates a new {@link ValidationViolation} for the provided object.
     *
     * @return The newly created {@link ValidationViolation}.
     */
    ValidationViolation createViolation();
}
