package com.group3.sem3exam.logic.validation;

@FunctionalInterface
public interface ResourceValidatorAttributeCheckConstructor<R, V>
{

    /**
     * Constructs some {@link ResourceValidatorAttributeCheck}.
     *
     * @param parentValue    The value of the object being validated.
     * @param attributeValue The value of the attribute on the object being validated.
     * @param attributeName  The name of the attribute being validated.
     * @return The newly created {@link ResourceValidatorAttributeCheck}.
     */
    ResourceValidatorAttributeCheck<R, V> construct(R parentValue, V attributeValue, String attributeName);
}
