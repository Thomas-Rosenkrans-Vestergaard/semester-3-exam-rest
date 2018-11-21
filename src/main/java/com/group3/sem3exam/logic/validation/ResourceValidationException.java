package com.group3.sem3exam.logic.validation;

import com.group3.sem3exam.logic.FacadeException;

import java.util.List;

/**
 * Thrown when a provided resource could not be validated.
 */
public class ResourceValidationException extends FacadeException
{

    /**
     * The complete list of violations, that caused the resource to not be valid.
     */
    private final List<ValidationViolation> validationViolations;

    /**
     * Creates a new {@link ResourceValidationException}.
     *
     * @param resourceClass The type of the resource that could not be validated.
     * @param violations    The complete list of violations, that caused the resource to not be valid.
     */
    public ResourceValidationException(Class resourceClass, List<ValidationViolation> violations)
    {
        super(
                String.format("%sValidationError", resourceClass.getSimpleName()),
                String.format("Could not validate provided resource %s", resourceClass.getSimpleName()),
                null);

        this.validationViolations = violations;
    }

    /**
     * Returns the complete list of violations, that caused the resource to not be valid.
     *
     * @return The complete list of violations, that caused the resource to not be valid.
     */
    public List<ValidationViolation> getValidationViolations()
    {
        return this.validationViolations;
    }
}
