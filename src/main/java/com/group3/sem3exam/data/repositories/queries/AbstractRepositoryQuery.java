package com.group3.sem3exam.data.repositories.queries;

import com.group3.sem3exam.data.repositories.queries.tree.Conditional;
import com.group3.sem3exam.data.repositories.queries.tree.Direction;
import com.group3.sem3exam.data.repositories.queries.tree.Operation;
import com.group3.sem3exam.data.repositories.queries.tree.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractRepositoryQuery<E> implements RepositoryQuery<E>
{

    protected Set<String>       allowedColumns;
    protected List<Conditional> wheres = new ArrayList<>();
    protected List<Order>       orders = new ArrayList<>();

    public AbstractRepositoryQuery(Set<String> allowedColumns)
    {
        this.allowedColumns = allowedColumns;
    }

    @Override
    public RepositoryQuery whereEq(String column, Object object)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.EQ, object)));

        return this;
    }

    @Override
    public RepositoryQuery whereNot(String column, Object object)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.NOT, object)));

        return this;
    }

    @Override
    public RepositoryQuery whereIn(String column, Object... objects)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery whereIn(String column, List objects)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery whereNotIn(String column, Object... objects)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.NOT_IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery whereNotIn(String column, List objects)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.NOT_IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery whereBetween(String column, Object start, Object end)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.BETWEEN, start, end)));

        return this;
    }

    @Override
    public RepositoryQuery whereOutside(String column, Object start, Object end)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.OUTSIDE, start, end)));

        return this;
    }

    @Override
    public RepositoryQuery whereLike(String column, Object object)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.LIKE, object)));

        return this;
    }

    @Override
    public RepositoryQuery whereNotLike(String column, Object object)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            wheres.add(Conditional.op(new Operation(column, Operation.Type.NOT_LIKE, object)));

        return this;
    }

    @Override
    public RepositoryQuery order(String column, Direction direction)
    {
        if (allowedColumns == null || allowedColumns.contains(column))
            orders.add(new Order(column, direction));

        return this;
    }
}
