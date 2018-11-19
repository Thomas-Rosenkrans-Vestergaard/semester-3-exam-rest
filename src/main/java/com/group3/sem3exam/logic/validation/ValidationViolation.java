package com.group3.sem3exam.logic.validation;

import java.io.Serializable;
import java.util.Map;

public class ValidationViolation
{

    private String              message;
    private String              checkName;
    private String              attribute;
    private Object              invalidValue;
    private Map<String, ? extends Serializable> variables;

    public ValidationViolation(String message, String checkName, String attribute, Object invalidValue, Map<String, ? extends Serializable> variables)
    {
        this.message = message;
        this.checkName = checkName;
        this.attribute = attribute;
        this.invalidValue = invalidValue;
        this.variables = variables;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getCheckName()
    {
        return this.checkName;
    }

    public String getAttribute()
    {
        return this.attribute;
    }

    public Object getInvalidValue()
    {
        return this.invalidValue;
    }

    public Map<String, ? extends Serializable> getVariables()
    {
        return this.variables;
    }
}
