package com.group3.sem3exam.data.repositories.queries.tree;

public class Operation
{
    public final String   attribute;
    public final Type     type;
    public final Object[] arguments;

    public Operation(String attribute, Type type, Object... arguments)
    {
        this.attribute = attribute;
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
