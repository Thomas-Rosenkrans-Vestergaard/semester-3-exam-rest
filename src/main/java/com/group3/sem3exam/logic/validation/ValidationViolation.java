package com.group3.sem3exam.logic.validation;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a violation of some validation in {@link ResourceValidator}.
 */
public class ValidationViolation
{

    /**
     * A user-oriented description of why the violation occurred.
     */
    private String message;

    /**
     * The name of the check that failed.
     */
    private String checkName;

    /**
     * The name of the attribute the violation occurred.
     */
    private String attribute;

    /**
     * The invalid value.
     */
    private Object invalidValue;

    /**
     * The variables associated with the check that failed.
     */
    private Map<String, ? extends Serializable> variables;

    /**
     * Creates a new {@link ValidationViolation}.
     *
     * @param message      A user-oriented description of why the violation occurred.
     * @param checkName    The name of the check that failed.
     * @param attribute    The name of the attribute the violation occurred.
     * @param invalidValue The invalid value.
     * @param variables    The variables associated with the check that failed.
     */
    public ValidationViolation(String message, String checkName, String attribute, Object invalidValue, Map<String, ? extends Serializable> variables)
    {
        this.message = message;
        this.checkName = checkName;
        this.attribute = attribute;
        this.invalidValue = invalidValue;
        this.variables = variables;
    }

    /**
     * Returns t user-oriented description of why the violation occurred.
     *
     * @return A user-oriented description of why the violation occurred.
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * Returns the name of the check that failed.
     *
     * @return The name of the check that failed.
     */
    public String getCheckName()
    {
        return this.checkName;
    }

    /**
     * Returns the name of the attribute the violation occurred.
     *
     * @return The name of the attribute the violation occurred.
     */
    public String getAttribute()
    {
        return this.attribute;
    }

    /**
     * Returns the invalid value.
     *
     * @return The invalid value.
     */
    public Object getInvalidValue()
    {
        return this.invalidValue;
    }

    /**
     * Returns the variables associated with the check that failed.
     *
     * @return The variables associated with the check that failed.
     */
    public Map<String, ? extends Serializable> getVariables()
    {
        return this.variables;
    }
}
