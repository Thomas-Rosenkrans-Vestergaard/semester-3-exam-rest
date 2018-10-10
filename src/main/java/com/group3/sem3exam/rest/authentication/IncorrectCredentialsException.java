package com.group3.sem3exam.rest.authentication;

public class IncorrectCredentialsException extends AuthenticationException
{
    public IncorrectCredentialsException()
    {
        this(null);
    }

    public IncorrectCredentialsException(Throwable cause)
    {
        super("IncorrectCredentialsError", "The provided credentials are incorrect.", 401, cause);
    }
}
