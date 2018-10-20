package com.group3.sem3exam.rest.authentication.jwt;

public class JwtSecretGenerationException extends Exception
{

    public JwtSecretGenerationException(Throwable cause)
    {
        super("Could not generate secret for Jwt tokens.", cause);
    }
}
