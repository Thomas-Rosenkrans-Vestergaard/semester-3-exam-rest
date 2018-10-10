package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.rest.exceptions.APIException;

public class AuthenticationException extends APIException
{

    public AuthenticationException()
    {
        this(null);
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
