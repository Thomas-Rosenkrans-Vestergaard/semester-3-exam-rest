package com.group3.sem3exam.data.repositories.queries;

public class RepositoryQueryCompileException extends RepositoryQueryException
{

    public RepositoryQueryCompileException(String message)
    {
        super(message);
    }

    public RepositoryQueryCompileException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
