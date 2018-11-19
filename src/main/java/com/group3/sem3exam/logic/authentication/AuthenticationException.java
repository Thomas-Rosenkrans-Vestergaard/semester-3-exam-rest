package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.logic.FacadeException;

public class AuthenticationException extends FacadeException
{

    public AuthenticationException()
    {
        this((Throwable) null);
    }

    public AuthenticationException(String errorMessage)
    {
        this("AuthenticationError", errorMessage, null);
    }

    public AuthenticationException(Throwable cause)
    {
        this("AuthenticationError", "Could not perform authentication.", cause);
    }

    public AuthenticationException(String errorName, String errorMessage, Throwable cause)
    {
        super(errorName, errorMessage, cause);
    }
}
