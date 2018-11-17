package com.group3.sem3exam.data.repositories.queries;

import com.group3.sem3exam.data.repositories.RepositoryEntity;
import com.group3.sem3exam.data.repositories.queries.tree.Conditional;
import com.group3.sem3exam.data.repositories.queries.tree.Direction;
import com.group3.sem3exam.data.repositories.queries.tree.Operation;
import com.group3.sem3exam.data.repositories.queries.tree.Order;

import java.util.List;

public abstract class AbstractRepositoryQuery<K extends Comparable<K>, E extends RepositoryEntity<K>>
        implements RepositoryQuery<K, E>
{

    /**
     * The where conditionals registered with the query.
     */
    protected final List<Conditional> wheres;

    /**
     * The order clauses registered with the query.
     */
    protected final List<Order> orders;

    /**
     * The number of results to skip.
     */
    protected int skip;

    /**
     * The maximum number of results to return.
     */
    protected int limit;

    /**
     * Creates a new {@link AbstractRepositoryQuery} with predefined where and order clauses.
     *
     * @param wheres The where conditionals registered with the query.
     * @param orders The order clauses registered with the query.
     * @param skip   The number of results the query should skip.
     * @param limit  The maximum number of results to return.
     */
    public AbstractRepositoryQuery(List<Conditional> wheres, List<Order> orders, int skip, int limit)
    {
        this.wheres = wheres;
        this.orders = orders;
        this.skip = skip;
        this.limit = limit;
    }

    @Override
    public RepositoryQuery<K, E> eq(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.EQ, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> not(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.NOT, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> gt(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.GT, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> lt(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.LT, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> gtoe(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.GTOE, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> ltoe(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.LTOE, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> in(String attribute, Object... objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> in(String attribute, List objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> notIn(String attribute, Object... objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.NOT_IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> notIn(String attribute, List objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.NOT_IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> between(String attribute, Object start, Object end)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.BETWEEN, start, end)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> outside(String attribute, Object start, Object end)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.OUTSIDE, start, end)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> like(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.LIKE, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> notLike(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.NOT_LIKE, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> order(String attribute, Direction direction)
    {
        orders.add(new Order(attribute, direction));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> skip(int n)
    {
        this.skip = Math.max(n, 0);

        return this;
    }

    @Override
    public RepositoryQuery<K, E> limit(int n)
    {
        this.limit = Math.max(n, 0);

        return this;
    }
}
