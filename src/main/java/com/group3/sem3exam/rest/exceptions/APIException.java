package com.group3.sem3exam.rest.exceptions;

import java.util.HashMap;
import java.util.Map;

public class APIException extends Exception
{
    private final String              errorName;
    private final String              errorMessage;
    private final int                 responseCode;
    private final Throwable           cause;
    private final Map<String, Object> debugVariables = new HashMap<>();

    public APIException(String errorName, String errorMessage, int responseCode, Throwable cause)
    {
        this.errorName = errorName;
        this.errorMessage = errorMessage;
        this.responseCode = responseCode;
        this.cause = cause;
    }

    public String getErrorName()
    {
        return this.errorName;
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    public int getResponseCode()
    {
        return this.responseCode;
    }

    @Override
    public Throwable getCause()
    {
        return this.cause;
    }

    public APIException debug(String key, Object value)
    {
        this.debugVariables.put(key, value);

        return this;
    }
}
