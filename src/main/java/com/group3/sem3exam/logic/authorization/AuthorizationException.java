package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.FacadeException;

public class AuthorizationException extends FacadeException
{

    public AuthorizationException(String errorMessage, Throwable cause)
    {
        super("AuthorizationError", errorMessage, cause);
    }

    public AuthorizationException(String errorMessage)
    {
        this(errorMessage, null);
    }
}
