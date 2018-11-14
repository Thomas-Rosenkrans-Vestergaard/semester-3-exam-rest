package com.group3.sem3exam.logic;

public class FacadeException extends Exception
{
    private final String    errorName;
    private final String    errorMessage;
    private final Integer   responseCode;
    private final Throwable cause;

    public FacadeException(String errorName, String errorMessage, Integer responseCode, Throwable cause)
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
}
