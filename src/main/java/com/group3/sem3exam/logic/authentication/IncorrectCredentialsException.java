package com.group3.sem3exam.logic.authentication;

public class IncorrectCredentialsException extends AuthenticationException
{

    /**
     * Creates a new {@link IncorrectCredentialsException} without a cause.
     */
    public IncorrectCredentialsException()
    {
        this(null);
    }

    /**
     * Creates a new {@link IncorrectCredentialsException} with a cause.
     *
     * @param cause The cause of the exception.
     */
    public IncorrectCredentialsException(Throwable cause)
    {
        super("IncorrectCredentialsError", "The provided credentials are incorrect.", 401, cause);
    }
}
