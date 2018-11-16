package com.group3.sem3exam.data.repositories.queries.tree;

public class Operation
{
    public final String   column;
    public final Type     type;
    public final Object[] arguments;

    public Operation(String column, Type type, Object... arguments)
    {
        this.column = column;
        this.type = type;
        this.arguments = arguments;
    }

    public enum Type
    {
        EQ,
        NOT,
        IN,
        NOT_IN,
        BETWEEN,
        OUTSIDE,
        LIKE,
        NOT_LIKE,
    }
}
