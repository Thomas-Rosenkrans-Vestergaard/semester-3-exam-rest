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
    public RepositoryQuery<K, E> whereEq(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.EQ, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereNot(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.NOT, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereIn(String attribute, Object... objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereIn(String attribute, List objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereNotIn(String attribute, Object... objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.NOT_IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereNotIn(String attribute, List objects)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.NOT_IN, objects)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereBetween(String attribute, Object start, Object end)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.BETWEEN, start, end)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereOutside(String attribute, Object start, Object end)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.OUTSIDE, start, end)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereLike(String attribute, Object object)
    {
        wheres.add(Conditional.op(new Operation(attribute, Operation.Type.LIKE, object)));

        return this;
    }

    @Override
    public RepositoryQuery<K, E> whereNotLike(String attribute, Object object)
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
