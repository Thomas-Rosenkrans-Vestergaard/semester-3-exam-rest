package com.group3.sem3exam.logic.validation;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.context.FieldContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourceValidator<R>
{

    /**
     * The subject to perform validation upon.
     */
    private final R subject;

    /**
     * The validation violation collected during validations.
     */
    private final List<ValidationViolation> violations = new ArrayList<>();

    /**
     * Creates a new {@link ResourceValidator}.
     *
     * @param subject The subject to perform validation upon.
     */
    public ResourceValidator(R subject)
    {
        this.subject = subject;
    }

    /**
     * Performs Oval validation on the subject. Any constraint violations are converted to validation violations,
     * and are added to the list of violations in this instance.
     *
     * @return {@code true} when there were no Oval constraint violations, {@code false} when there were Oval constraint
     * violations.
     */
    public boolean oval()
    {
        Validator                 validator            = new Validator();
        List<ConstraintViolation> constraintViolations = validator.validate(subject);
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
     * Throws a new {@link ResourceValidationException} of the type of the encapsulated subject, with the violation
     * violations.
     *
     * @throws ResourceValidationException The new exception.
     */
    public void throwResourceValidationException() throws ResourceValidationException
    {
        if (hasErrors())
            throw new ResourceValidationException(subject.getClass(), violations);
    }

    /**
     * Returns an {@link ResourceValidatorAttribute} for the attribute with provided attribute name.
     *
     * @param attribute The name of the attribute to perform checks on.
     * @param type      The class of the value on the attribute to perform checks on.
     * @param <T>       The type of the value on the attribute to perform checks on.
     * @return The {@link ResourceValidatorAttribute}.
     */
    public <T> ResourceValidatorAttribute<R, T> on(String attribute, Class<T> type)
    {
        try {

            Field field = subject.getClass().getField(attribute);
            field.setAccessible(true);
            T casted = type.cast(field.get(subject));

            return new Attribute<>(subject, casted, attribute);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * The internal implementation of {@link ResourceValidatorAttribute}.
     *
     * @param <V> The value in the attribute.
     */
    private class Attribute<V> implements ResourceValidatorAttribute<R, V>
    {

        /**
         * The parent value.
         */
        private final R r;

        /**
         * The attribute value.
         */
        private final V v;

        /**
         * The name of the attribute.
         */
        private final String attributeName;

        /**
         * Creates a new {@link Attribute}.
         *
         * @param r             The parent value.
         * @param v             The attribute value.
         * @param attributeName The attribute name.
         */
        public Attribute(R r, V v, String attributeName)
        {
            this.r = r;
            this.v = v;
            this.attributeName = attributeName;
        }

        @Override
        public ResourceValidatorAttribute<R, V> check(ResourceValidatorAttributeCheckConstructor<R, V> constructor)
        {
            ResourceValidatorAttributeCheck<R, V> check = constructor.construct(r, v, attributeName);
            if (!check.passes())
                ResourceValidator.this.violations.add(check.createViolation());

            return this;
        }
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
        String attributeName = getAttributeName(constraintViolation);
        String message       = constraintViolation.getMessage();

        return new ValidationViolation(
                "Provided " + attributeName + message.substring(message.indexOf(" ")),
                getCheckName(constraintViolation),
                attributeName,
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
        String attributeName = getAttributeName(parent) + '.' + getAttributeName(child);
        String message       = child.getMessage();

        return new ValidationViolation(
                "Provided " + attributeName + message.substring(message.indexOf(" ")),
                getCheckName(child),
                attributeName,
                child.getInvalidValue(),
                child.getMessageVariables()
        );
    }

    /**
     * Returns the check name of a provided {@link ConstraintViolation}. The check name is the name of the
     * annotation check that failed on the subject.
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
