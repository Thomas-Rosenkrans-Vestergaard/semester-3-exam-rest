package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.facades.FacadeException;

public class AuthenticationException extends FacadeException
{

    public AuthenticationException()
    {
        this((Throwable) null);
    }

    public AuthenticationException(String errorMessage)
    {
        this("AuthenticationError", errorMessage, 401, null);
    }

    public AuthenticationException(Throwable cause)
    {
        this("AuthenticationError", "Could not perform authentication.", 401, cause);
    }

    public AuthenticationException(String errorName, String errorMessage, int responseCode, Throwable cause)
    {
        super(errorName, errorMessage, responseCode, cause);
    }
}
