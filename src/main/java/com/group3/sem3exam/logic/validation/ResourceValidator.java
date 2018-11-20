package com.group3.sem3exam.logic.validation;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.context.FieldContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourceValidator<R>
{

    /**
     * The object to perform validation upon.
     */
    private final R object;

    /**
     * The validation violation collected during validations.
     */
    private final List<ValidationViolation> violations = new ArrayList<>();

    /**
     * Creates a new {@link ResourceValidator}.
     *
     * @param object The object to perform validation upon.
     */
    public ResourceValidator(R object)
    {
        if (object == null)
            throw new IllegalArgumentException("The object provided to ResourceValidator must not be null.");

        this.object = object;
    }

    /**
     * Performs the provided check on the object being validated.
     *
     * @param condition The condition that must be true.
     * @return The result of the condition check.
     */
    public <V> boolean check(ResourceValidatorCondition<R, V> condition)
    {
        V subject = condition.getFactory().apply(this.object);
        boolean result = condition.isTrue(subject);
        if (!result)
            violations.add(condition.createViolation(subject));

        return result;
    }

    /**
     * Performs Oval validation on the object. Any constraint violations are converted to validation violations,
     * and are added to the list of violations in this instance.
     *
     * @return {@code true} when there were no Oval constraint violations, {@code false} when there were Oval constraint
     * violations.
     */
    public boolean oval()
    {
        Validator                 validator            = new Validator();
        List<ConstraintViolation> constraintViolations = validator.validate(object);
        for (ConstraintViolation constraintViolation : constraintViolations) {
            violations.addAll(toValidationViolations(constraintViolation));
        }

        return constraintViolations.isEmpty();
    }

    /**
     * Checks if there are {@link ValidationViolation}s in the {@link ResourceValidator}.
     *
     * @return {@code true} when this {@link ResourceValidator} has validation violations.
     */
    public boolean hasErrors()
    {
        return !violations.isEmpty();
    }

    /**
     * Throws a new {@link ResourceValidationException} of the type of the encapsulated object, with the violation
     * violations.
     *
     * @throws ResourceValidationException The new exception.
     */
    public void throwResourceValidationException() throws ResourceValidationException
    {
        if (hasErrors())
            throw new ResourceValidationException(object.getClass(), violations);
    }

    /**
     * Converts the provided constraint violation into a collection of validation violations.
     *
     * @param constraintViolation The constraint violation to convert into a collection of validation violations.
     * @return The collection of validation violations.
     */
    private Collection<ValidationViolation> toValidationViolations(ConstraintViolation constraintViolation)
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

    /**
     * Creates a new {@link ValidationViolation} from a provided {@link ConstraintViolation}.
     *
     * @param constraintViolation The {@link ConstraintViolation} to convert to a {@link ValidationViolation}.
     * @return The resulting {@link ValidationViolation}.
     */
    private ValidationViolation toValidationViolation(ConstraintViolation constraintViolation)
    {
        return new ValidationViolation(
                "Provided " + constraintViolation.getMessage(),
                getCheckName(constraintViolation),
                getAttributeName(constraintViolation),
                constraintViolation.getInvalidValue(),
                constraintViolation.getMessageVariables()
        );
    }

    /**
     * Creates a new {@link ValidationViolation} from a parent and child {@link ConstraintViolation}.
     *
     * @param parent The parent violation.
     * @param child  The child violation.
     * @return The resulting {@link ValidationViolation}.
     */
    private ValidationViolation toValidationViolation(ConstraintViolation parent, ConstraintViolation child)
    {
        return new ValidationViolation(
                "Provided " + child.getMessage(),
                getCheckName(child),
                getAttributeName(parent) + '.' + getAttributeName(child),
                child.getInvalidValue(),
                child.getMessageVariables()
        );
    }

    /**
     * Returns the check name of a provided {@link ConstraintViolation}. The check name is the name of the
     * annotation check that failed on the object.
     *
     * @param constraintViolation The {@link ConstraintViolation} from which to extract the check name.
     * @return The extracted check name.
     */
    private static String getCheckName(ConstraintViolation constraintViolation)
    {
        return constraintViolation.getCheckName().substring(constraintViolation.getCheckName().lastIndexOf(".") + 1);
    }

    /**
     * Returns the attribute of a provided {@link ConstraintViolation}. The attribute is the name of the field name on
     * which the Oval check failed.
     *
     * @param constraintViolation The {@link ConstraintViolation} from which to extract the attribute name.
     * @return The extracted attribute name.
     */
    private static String getAttributeName(ConstraintViolation constraintViolation)
    {
        if (!(constraintViolation.getContext() instanceof FieldContext))
            return null;

        FieldContext fieldContext = (FieldContext) constraintViolation.getContext();

        return fieldContext.getField().getName();
    }
}
