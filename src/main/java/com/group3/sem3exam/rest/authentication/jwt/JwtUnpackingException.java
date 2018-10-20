package com.group3.sem3exam.rest.authentication.jwt;

public class JwtUnpackingException extends Exception
{
    public JwtUnpackingException(String message)
    {
        super(message);
    }

    public JwtUnpackingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
