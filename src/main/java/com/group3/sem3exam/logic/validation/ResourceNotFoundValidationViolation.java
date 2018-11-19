package com.group3.sem3exam.logic.validation;

import java.util.HashMap;

/**
 * Thrown when some resource cannot be found.
 */
public class ResourceNotFoundValidationViolation extends ValidationViolation
{

    /**
     * Creates a new {@link ResourceValidationException}.
     *
     * @param resourceClass The type of resource that could not be found.
     * @param attribute     The attribute the value that could not be found was on.
     * @param invalidValue  The invalid value.
     */
    public ResourceNotFoundValidationViolation(Class resourceClass, String attribute, Object invalidValue)
    {
        super(String.format("Could not find provided %s.", resourceClass.getSimpleName()),
              "ResourceExistsCheck",
              attribute,
              invalidValue,
              new HashMap<>());
    }
}
