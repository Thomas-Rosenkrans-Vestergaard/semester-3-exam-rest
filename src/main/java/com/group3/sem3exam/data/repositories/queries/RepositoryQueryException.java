package com.group3.sem3exam.data.repositories.queries;

public class RepositoryQueryException extends RuntimeException
{

    public RepositoryQueryException(String message)
    {
        super(message);
    }

    public RepositoryQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
