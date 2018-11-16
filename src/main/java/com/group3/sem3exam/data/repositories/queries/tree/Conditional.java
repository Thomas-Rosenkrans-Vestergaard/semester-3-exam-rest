package com.group3.sem3exam.data.repositories.queries.tree;

import static com.group3.sem3exam.data.repositories.queries.tree.Conditional.Type.*;

public class Conditional
{
    public final Type        type;
    public final Conditional left;
    public final Conditional right;
    public final Operation   operation;

    public Conditional(Type type, Conditional left, Conditional right, Operation operation)
    {
        this.type = type;
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public static Conditional and(Conditional left, Conditional right)
    {
        return new Conditional(AND, left, right, null);
    }

    public static Conditional or(Conditional left, Conditional right)
    {
        return new Conditional(OR, left, right, null);
    }

    public static Conditional op(Operation operation)
    {
        return new Conditional(OP, null, null, operation);
    }

    public enum Type
    {
        AND,
        OR,
        OP
    }
}
