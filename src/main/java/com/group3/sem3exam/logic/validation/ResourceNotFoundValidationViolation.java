package com.group3.sem3exam.logic.validation;

import java.util.HashMap;

public class ResourceNotFoundValidationViolation extends ValidationViolation
{

    public ResourceNotFoundValidationViolation(Class resourceClass, String attribute, Object invalidValue)
    {
        super(String.format("Could not find provided %s.", resourceClass.getSimpleName()),
              "ResourceExistsCheck",
              attribute,
              invalidValue,
              new HashMap<>());
    }
}
