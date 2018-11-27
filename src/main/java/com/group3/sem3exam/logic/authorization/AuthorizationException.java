package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.FacadeException;

public class AuthorizationException extends FacadeException
{


    public AuthorizationException(String errorName, String errorMessage, Throwable cause)
    {
        super(errorName, errorMessage, cause);
    }

    public AuthorizationException(String errorMessage, Throwable cause)
    {
        this("AuthorizationError", errorMessage, cause);
    }

    public AuthorizationException(String errorMessage)
    {
        this(errorMessage, null);
    }
}
