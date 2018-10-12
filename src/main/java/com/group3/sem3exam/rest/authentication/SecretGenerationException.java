package com.group3.sem3exam.rest.authentication;

public class SecretGenerationException extends Exception
{

    public SecretGenerationException(Throwable cause)
    {
        super("Could not generate secret for Jwt tokens.", cause);
    }
}
