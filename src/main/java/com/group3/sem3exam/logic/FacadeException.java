package com.group3.sem3exam.logic;

public class FacadeException extends Exception
{

    private final String    errorName;
    private final String    errorMessage;

    public FacadeException(String errorName, String errorMessage, Throwable cause)
    {
        super(cause);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    public String getErrorName()
    {
        return this.errorName;
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }
}
