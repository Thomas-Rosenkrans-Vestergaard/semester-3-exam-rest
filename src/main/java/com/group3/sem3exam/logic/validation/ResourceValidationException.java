package com.group3.sem3exam.logic.validation;

import com.group3.sem3exam.logic.FacadeException;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.context.FieldContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourceValidationException extends FacadeException
{

    private final List<ValidationViolation> validationViolations;

    public ResourceValidationException(Class resourceClass, List<ValidationViolation> violations)
    {
        super(
                String.format("%sValidationError", resourceClass.getSimpleName()),
                String.format("Could not validate provided resource %s", resourceClass.getSimpleName()),
                null);

        this.validationViolations = violations;
    }

    public List<ValidationViolation> getValidationViolations()
    {
        return this.validationViolations;
    }

    public static ResourceValidationException oval(Class resourceClass, List<ConstraintViolation> constraintViolations)
    {
        List<ValidationViolation> validationViolations = new ArrayList<>();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            validationViolations.addAll(toValidationViolations(constraintViolation));
        }

        return new ResourceValidationException(resourceClass, validationViolations);
    }

    private static Collection<ValidationViolation> toValidationViolations(ConstraintViolation constraintViolation)
    {
        List<ValidationViolation> validationViolations = new ArrayList<>();
        ConstraintViolation[]     causes               = constraintViolation.getCauses();
        if (causes == null)
            validationViolations.add(toValidationViolation(constraintViolation));
        else
            for (ConstraintViolation cause : causes)
                validationViolations.add(toValidationViolation(constraintViolation, cause));

        return validationViolations;
    }

    private static ValidationViolation toValidationViolation(ConstraintViolation constraintViolation)
    {
        return new ValidationViolation(
                constraintViolation.getMessage(),
                getCheckName(constraintViolation),
                getAttributeName(constraintViolation),
                constraintViolation.getInvalidValue(),
                constraintViolation.getMessageVariables()
        );
    }

    private static ValidationViolation toValidationViolation(ConstraintViolation parent, ConstraintViolation child)
    {
        return new ValidationViolation(
                child.getMessage(),
                getCheckName(child),
                getAttributeName(parent) + '.' + getAttributeName(child),
                child.getInvalidValue(),
                child.getMessageVariables()
        );
    }

    private static String getCheckName(ConstraintViolation constraintViolation)
    {
        return constraintViolation.getCheckName().substring(constraintViolation.getCheckName().lastIndexOf(".") + 1);
    }

    private static String getAttributeName(ConstraintViolation constraintViolation)
    {
        if (!(constraintViolation.getContext() instanceof FieldContext))
            return null;

        FieldContext fieldContext = (FieldContext) constraintViolation.getContext();

        return fieldContext.getField().getName();
    }
}
