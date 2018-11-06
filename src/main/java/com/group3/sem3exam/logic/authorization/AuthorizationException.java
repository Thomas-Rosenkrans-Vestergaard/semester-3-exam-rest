package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.FacadeException;

public class AuthorizationException extends FacadeException
{

    public AuthorizationException(String errorName, String errorMessage, Integer responseCode, Throwable cause)
    {
        super(errorName, errorMessage, responseCode, cause);
    }
}
