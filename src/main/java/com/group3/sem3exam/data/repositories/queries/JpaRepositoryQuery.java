package com.group3.sem3exam.data.repositories.queries;

import com.group3.sem3exam.data.repositories.RepositoryEntity;
import com.group3.sem3exam.data.repositories.queries.tree.Conditional;
import com.group3.sem3exam.data.repositories.queries.tree.Operation;
import com.group3.sem3exam.data.repositories.queries.tree.Order;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class JpaRepositoryQuery<K extends Comparable<K>, E extends RepositoryEntity<K>> extends AbstractRepositoryQuery<K, E>
{

    /**
     * The entity manager the query is called upon.
     */
    private final EntityManager entityManager;

    /**
     * The type of the key of the entity queried.
     */
    private final Class<K> kClass;

    /**
     * The key attributes of the entity queried.
     */
    private final String kAttribute;

    /**
     * The type of entity queried.
     */
    private final Class<E> eClass;

    /**
     * The string prefixed to the attributes compiled to JPQL.
     */
    private final String prefix;

    /**
     * Creates a new {@link JpaRepositoryQuery} with the provided list of .
     *
     * @param entityManager The entity manager instances the query is called upon.
     * @param kClass        The type of the key of the entity queried.
     * @param kAttribute    The key attributes of the entity queried.
     * @param eClass        The type of entity queried.
     * @param prefix        The string prefixed to the attributes compiled to JPQL.
     * @param wheres        The where conditionals to register with the query.
     * @param orders        The order clauses to register with the query.
     * @param skip          The number of results the query should skip.
     * @param limit         The maximum number of results to return.
     */
    public JpaRepositoryQuery(
            EntityManager entityManager,
            Class<K> kClass,
            String kAttribute,
            Class<E> eClass,
            String prefix,
            List<Conditional> wheres,
            List<Order> orders,
            int skip,
            int limit)
    {
        super(wheres, orders, skip, limit);
        this.entityManager = entityManager;
        this.kClass = kClass;
        this.kAttribute = kAttribute;
        this.eClass = eClass;
        this.prefix = prefix;
    }

    /**
     * Creates a new {@link JpaRepositoryQuery}.
     *
     * @param entityManager The entity manager instance the query is called upon.
     * @param kClass        The type of the key of the entity queried.
     * @param kAttribute    The key attributes of the entity queried.
     * @param eClass        The type of entity queried.
     * @param prefix        The string prefixed to the attributes compiled to JPQL.
     */
    public JpaRepositoryQuery(EntityManager entityManager, Class<K> kClass, String kAttribute, Class<E> eClass, String prefix)
    {
        this(entityManager, kClass, kAttribute, eClass, prefix, new ArrayList<>(), new ArrayList<>(), 0, Integer.MAX_VALUE);
    }

    /**
     * Creates a new {@link JpaRepositoryQuery}.
     *
     * @param entityManager The entity manager instance the query is called upon.
     * @param kClass        The type of the key of the entity queried.
     * @param kAttribute    The key attributes of the entity queried.
     * @param eClass        The type of entity queried.
     */
    public JpaRepositoryQuery(EntityManager entityManager, Class<K> kClass, String kAttribute, Class<E> eClass)
    {
        this(entityManager, kClass, kAttribute, eClass, eClass.getSimpleName().substring(0, 1).toLowerCase());
    }

    /**
     * Executes the query, returning all results.
     *
     * @return All the results in the query.
     */
    @Override
    public List<E> get()
    {
        return createDataQuery(entityManager)
                .setMaxResults(this.limit)
                .setFirstResult(this.skip)
                .getResultList();
    }

    /**
     * Executes the query returning the result of a single page.
     *
     * @param pageSize   The number of results on a single page. Where {@code pageSize >= 1}.
     * @param pageNumber The page to return the results from. Where {@code pageNumber >= 1}.
     * @return The results in the page.
     */
    @Override
    public List<E> getPage(int pageSize, int pageNumber)
    {
        pageSize = Math.max(pageSize, 1);
        pageNumber = Math.max(pageNumber, 1);

        return createDataQuery(entityManager)
                .setMaxResults(Math.min(pageSize, this.limit))
                .setFirstResult(this.skip + (pageSize * (pageNumber - 1)))
                .getResultList();
    }

    /**
     * Returns the first {@code n} result of the query. The operation does not throw an exception when the
     * results are out of bounds.
     *
     * @param n The number of results to retrieve.
     * @return The first {@code n} results of the query.
     */
    @Override
    public List<E> getFirst(int n)
    {
        return createDataQuery(entityManager)
                .setMaxResults(Math.min(n, this.limit))
                .setFirstResult(this.skip)
                .getResultList();
    }

    /**
     * Returns the {@code n}th result of the query.
     *
     * @param n The index of the element to return. {@code n} starts at 0.
     * @return The {@code n}th result of the query. {@code null} when the result is out of bounds.
     */
    @Override
    public E getAt(int n)
    {
        try {
            return createDataQuery(entityManager)
                    .setMaxResults(Math.min(1, this.limit))
                    .setFirstResult(this.skip + n)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public long count()
    {
        return createCountQuery(entityManager)
                .getSingleResult();
    }

    @Override
    public boolean exists()
    {
        return count() > 0;
    }

    @Override
    public boolean contains(K key)
    {
        return whereEq(kAttribute, key).exists();
    }

    @Override
    public <V> V max(String attribute, Class<V> vClass)
    {
        try {
            TypedQuery<V> query = entityManager
                    .createQuery(String.format("SELECT max(%s) FROM %s %s %s",
                                               prefix + "." + attribute,
                                               eClass.getSimpleName(),
                                               prefix,
                                               generateJPQL()), vClass);

            bindJPQL(query);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public <V> V min(String attribute, Class<V> vClass)
    {
        try {
            TypedQuery<V> query = entityManager
                    .createQuery(String.format("SELECT min(%s) FROM %s %s %s",
                                               prefix + "." + attribute,
                                               eClass.getSimpleName(),
                                               prefix,
                                               generateJPQL()), vClass);

            bindJPQL(query);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public <V> List<V> getAttributes(String attribute, Class<V> vClass)
    {
        try {
            TypedQuery<V> query = entityManager
                    .createQuery(String.format("SELECT %s FROM %s %s %s",
                                               prefix + "." + attribute,
                                               eClass.getSimpleName(),
                                               prefix,
                                               generateJPQL()), vClass);

            bindJPQL(query);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<K> getKeys()
    {
        try {
            TypedQuery<K> query = entityManager
                    .createQuery(String.format("SELECT %s FROM %s %s %s",
                                               prefix + "." + kAttribute,
                                               eClass.getSimpleName(),
                                               prefix,
                                               generateJPQL()), kClass);

            bindJPQL(query);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean chunk(int chunkSize, Chunker<E> chunker)
    {
        MutableChunk<E> chunk   = new MutableChunk<>();
        Stopper         stopper = new Stopper();

        for (int chunkNumber = 1; !stopper.stopped; chunkNumber++) {
            chunk.index = chunkNumber - 1;
            chunk.position = chunkNumber;
            chunk.results = getPage(chunkSize, chunkNumber);
            if (chunk.results.isEmpty())
                return true;

            chunker.handle(chunk, stopper);
            entityManager.flush();
            entityManager.clear();
        }

        return false;
    }

    public class Stopper implements Runnable
    {

        public boolean stopped = false;

        @Override
        public void run()
        {
            this.stopped = true;
        }
    }

    private class MutableChunk<E> implements Chunk<E>
    {

        public int     index;
        public int     position;
        public List<E> results;

        public int index()
        {
            return index;
        }

        public int position()
        {
            return position;
        }

        @Override
        public List<E> getResults()
        {
            return results;
        }
    }

    @Override
    public RepositoryQuery<K, E> copy()
    {
        return new JpaRepositoryQuery<>(this.entityManager,
                                        this.kClass,
                                        this.kAttribute,
                                        this.eClass,
                                        this.prefix,
                                        new ArrayList<>(this.wheres),
                                        new ArrayList<>(this.orders),
                                        this.skip,
                                        this.limit);
    }

    private TypedQuery<Long> createCountQuery(EntityManager entityManager)
    {
        TypedQuery<Long> query = entityManager.createQuery(String.format("SELECT count(%s) FROM %s %s %s",
                                                                         prefix,
                                                                         eClass.getSimpleName(),
                                                                         prefix,
                                                                         generateJPQL()), Long.class);
        bindJPQL(query);

        return query;
    }

    private TypedQuery<E> createDataQuery(EntityManager entityManager)
    {
        TypedQuery<E> query = entityManager.createQuery(String.format("SELECT %s FROM %s %s %s",
                                                                      prefix,
                                                                      eClass.getSimpleName(),
                                                                      prefix,
                                                                      generateJPQL()), eClass);

        bindJPQL(query);

        return query;
    }

    private String generateJPQL()
    {
        StringBuilder    builder          = new StringBuilder();
        ParameterCounter parameterCounter = new ParameterCounter();

        if (wheres.size() > 0)
            generateWheres(builder, parameterCounter);
        if (orders.size() > 0)
            generateOrders(builder, parameterCounter);

        return builder.toString();
    }

    private void generateWheres(StringBuilder builder, ParameterCounter parameterCounter)
    {
        builder.append("WHERE true = true ");
        for (Conditional conditional : wheres) {
            builder.append(" AND ");
            generateConditional(builder, parameterCounter, conditional);
        }
    }

    private void generateConditional(StringBuilder builder, ParameterCounter parameterCounter, Conditional conditional)
    {
        switch (conditional.type) {
            case OP:
                generateOpCondition(builder, parameterCounter, conditional.operation);
                return;
            case AND:
                generateAndCondition(builder, parameterCounter, conditional.left, conditional.right);
                return;
            case OR:
                generateOrCondition(builder, parameterCounter, conditional.left, conditional.right);
                return;
            default:
                throw new RepositoryQueryCompileException("Missing condition type.");
        }
    }

    private void generateAndCondition(StringBuilder builder, ParameterCounter counter, Conditional left, Conditional right)
    {
        builder.append('(');
        generateConditional(builder, counter, left);
        builder.append(" AND ");
        generateConditional(builder, counter, right);
        builder.append(')');
    }

    private void generateOrCondition(StringBuilder builder, ParameterCounter counter, Conditional left, Conditional right)
    {

        builder.append('(');
        generateConditional(builder, counter, left);
        builder.append(" OR ");
        generateConditional(builder, counter, right);
        builder.append(')');
    }

    private void generateOpCondition(StringBuilder builder, ParameterCounter counter, Operation operation)
    {
        Operation.Type type = operation.type;

        switch (type) {
            case EQ:
                builder.append(prefix(operation.attribute));
                builder.append(" = ");
                builder.append(parameter(counter));
                return;
            case NOT:
                builder.append(prefix(operation.attribute));
                builder.append(" != ");
                builder.append(parameter(counter));
                return;
            case IN:
                builder.append(prefix(operation.attribute));
                builder.append(" IN ");
                builder.append(parameter(counter));
                return;
            case NOT_IN:
                builder.append(prefix(operation.attribute));
                builder.append(" NOT IN ");
                builder.append(parameter(counter));
                return;
            case BETWEEN:
                builder.append(prefix(operation.attribute));
                builder.append(" BETWEEN ");
                builder.append(parameter(counter));
                builder.append(" AND ");
                builder.append(parameter(counter));
                return;
            case OUTSIDE:
                builder.append(prefix(operation.attribute));
                builder.append(" NOT BETWEEN ");
                builder.append(parameter(counter));
                builder.append(" AND ");
                builder.append(parameter(counter));
                return;
            case LIKE:
                builder.append(prefix(operation.attribute));
                builder.append(" LIKE ");
                builder.append(parameter(counter));
                return;
            case NOT_LIKE:
                builder.append(prefix(operation.attribute));
                builder.append(" NOT LIKE ");
                builder.append(parameter(counter));
                return;
        }

        throw new RepositoryQueryCompileException("Unknown operation type " + type.toString());
    }

    private void generateOrders(StringBuilder builder, ParameterCounter parameterCounter)
    {
        builder.append(" ORDER BY ");
        int size = orders.size();
        for (int i = 0; i < size; i++) {
            Order order = orders.get(i);
            builder.append(prefix(order.attribute));
            builder.append(' ');
            builder.append(order.direction);
            if (i < size - 1)
                builder.append(", ");
        }
    }

    private void bindJPQL(TypedQuery<? extends Object> query)
    {
        ParameterCounter parameterCounter = new ParameterCounter();

        if (wheres.size() > 0)
            bindWheres(query, parameterCounter);
    }

    private void bindWheres(TypedQuery<? extends Object> query, ParameterCounter parameterCounter)
    {
        for (Conditional conditional : wheres) {
            bindConditional(query, parameterCounter, conditional);
        }
    }

    private void bindConditional(TypedQuery<? extends Object> query, ParameterCounter parameterCounter, Conditional conditional)
    {
        switch (conditional.type) {
            case OP:
                bindOpCondition(query, parameterCounter, conditional.operation);
                return;
            case AND:
                bindAndCondition(query, parameterCounter, conditional.left, conditional.right);
                return;
            case OR:
                bindOrCondition(query, parameterCounter, conditional.left, conditional.right);
                return;
            default:
                throw new RepositoryQueryCompileException("Missing condition type.");
        }
    }

    private void bindAndCondition(
            TypedQuery<? extends Object> query, ParameterCounter counter, Conditional left, Conditional
            right)
    {
        bindConditional(query, counter, left);
        bindConditional(query, counter, right);
    }

    private void bindOrCondition(
            TypedQuery<? extends Object> query, ParameterCounter counter, Conditional left, Conditional
            right)
    {
        bindConditional(query, counter, left);
        bindConditional(query, counter, right);
    }

    private void bindOpCondition(TypedQuery<? extends Object> query, ParameterCounter counter, Operation operation)
    {
        Operation.Type type = operation.type;

        switch (type) {
            case EQ:
                bind(query, counter, operation.attribute, argument(operation, 0));
                return;
            case NOT:
                bind(query, counter, operation.attribute, argument(operation, 0));
                return;
            case IN:
                bind(query, counter, operation.attribute, argument(operation, 0));
                return;
            case NOT_IN:
                bind(query, counter, operation.attribute, argument(operation, 0));
                return;
            case BETWEEN:
                bind(query, counter, operation.attribute, argument(operation, 0));
                bind(query, counter, operation.attribute, argument(operation, 1));
                return;
            case OUTSIDE:
                bind(query, counter, operation.attribute, argument(operation, 0));
                bind(query, counter, operation.attribute, argument(operation, 1));
                return;
            case LIKE:
                bind(query, counter, operation.attribute, '%' + argument(operation, 0).toString() + '%');
                return;
            case NOT_LIKE:
                bind(query, counter, operation.attribute, '%' + argument(operation, 0).toString() + '%');
                return;
        }

        throw new RepositoryQueryCompileException("Unknown operation type " + type.toString());
    }

    private void bind(TypedQuery<? extends Object> query, ParameterCounter parameterCounter, String parameter, Object value)
    {
        query.setParameter("parameter_" + parameterCounter.next(), value);
    }

    private String prefix(String attribute)
    {
        return prefix + '.' + attribute;
    }

    private String parameter(ParameterCounter parameterCounter)
    {
        return ":parameter_" + parameterCounter.next();
    }

    private Object argument(Operation operation, int n)
    {
        if (n >= operation.arguments.length)
            throw new RepositoryQueryCompileException("Missing argument at index " + n);

        return operation.arguments[n];
    }

    private class ParameterCounter
    {
        private int x = 0;

        public int next()
        {
            return ++x;
        }
    }
}
