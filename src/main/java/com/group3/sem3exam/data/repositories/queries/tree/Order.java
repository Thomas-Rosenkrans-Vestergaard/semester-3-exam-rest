package com.group3.sem3exam.data.repositories.queries.tree;

public class Order
{
    public final String    column;
    public final Direction direction;

    public Order(String column, Direction direction)
    {
        this.column = column;
        this.direction = direction;
    }

    public static Order desc(String column)
    {
        return new Order(column, Direction.DESC);
    }

    public static Order asc(String column)
    {
        return new Order(column, Direction.ASC);
    }
}
