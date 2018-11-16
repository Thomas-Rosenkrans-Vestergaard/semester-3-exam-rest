package com.group3.sem3exam.data.repositories.queries;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.queries.tree.Conditional;
import com.group3.sem3exam.data.repositories.queries.tree.Operation;
import com.group3.sem3exam.data.repositories.queries.tree.Order;
import com.group3.sem3exam.rest.JpaConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

public class JpaRepositoryQuery<E> extends AbstractRepositoryQuery<E>
{

    private final EntityManager entityManager;
    private final Class<E>      c;

    public JpaRepositoryQuery(Class<E> c, EntityManager entityManager)
    {
        this(c, entityManager, null);
    }

    public JpaRepositoryQuery(Class<E> c, EntityManager entityManager, Set<String> allowedColumns)
    {
        super(allowedColumns);

        this.entityManager = entityManager;
        this.c = c;
    }

    private class ParameterCounter
    {
        public int x = 0;
    }

    public static void main(String[] args)
    {
        JpaRepositoryQuery<User> query = (JpaRepositoryQuery<User>) new JpaRepositoryQuery<>(User.class, JpaConnection.create().createEntityManager())
                .whereEq("name", "e1")
                .whereNot("name", "e2")
                .whereIn("name", "e3", "e4", "e5")
                .whereNotIn("name", "e6", "e7", "e8")
                .whereBetween("name", "e9", "e10")
                .whereOutside("name", "e11", "e12")
                .whereLike("name", "e13")
                .desc("name")
                .asc("name");

        List<User> user = query.getAll();
        System.out.println(user.size());
    }

    public String generateJPQL()
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


        if (type == Operation.Type.EQ) {
            builder.append(prefix(operation.column));
            builder.append(" = ");
            builder.append(parameter(counter));
            return;
        }

        if (type == Operation.Type.NOT) {
            builder.append(prefix(operation.column));
            builder.append(" != ");
            builder.append(parameter(counter));
            return;
        }

        if (type == Operation.Type.IN) {
            builder.append(prefix(operation.column));
            builder.append(" IN ");
            builder.append(parameter(counter));
            return;
        }

        if (type == Operation.Type.NOT_IN) {
            builder.append(prefix(operation.column));
            builder.append(" NOT IN ");
            builder.append(parameter(counter));
            return;
        }

        if (type == Operation.Type.BETWEEN) {
            builder.append(prefix(operation.column));
            builder.append(" BETWEEN ");
            builder.append(parameter(counter));
            builder.append(" AND ");
            builder.append(parameter(counter));
            return;
        }

        if (type == Operation.Type.OUTSIDE) {
            builder.append(prefix(operation.column));
            builder.append(" NOT BETWEEN ");
            builder.append(parameter(counter));
            builder.append(" AND ");
            builder.append(parameter(counter));
            return;
        }

        if (type == Operation.Type.LIKE) {
            builder.append(prefix(operation.column));
            builder.append(" LIKE ");
            builder.append(parameter(counter));
            return;
        }

        if (type == Operation.Type.NOT_LIKE) {
            builder.append(prefix(operation.column));
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
            builder.append(prefix(order.column));
            builder.append(' ');
            builder.append(order.direction);
            if (i < size - 1)
                builder.append(", ");
        }
    }

    public void bindJPQL(TypedQuery<E> query)
    {
        ParameterCounter parameterCounter = new ParameterCounter();

        if (wheres.size() > 0)
            bindWheres(query, parameterCounter);
    }

    private void bindWheres(TypedQuery<E> query, ParameterCounter parameterCounter)
    {
        for (Conditional conditional : wheres) {
            bindConditional(query, parameterCounter, conditional);
        }
    }

    private void bindConditional(TypedQuery<E> query, ParameterCounter parameterCounter, Conditional conditional)
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

    private void bindAndCondition(TypedQuery<E> query, ParameterCounter counter, Conditional left, Conditional right)
    {
        bindConditional(query, counter, left);
        bindConditional(query, counter, right);
    }

    private void bindOrCondition(TypedQuery<E> query, ParameterCounter counter, Conditional left, Conditional right)
    {
        bindConditional(query, counter, left);
        bindConditional(query, counter, right);
    }

    private void bindOpCondition(TypedQuery<E> query, ParameterCounter counter, Operation operation)
    {
        Operation.Type type = operation.type;

        if (type == Operation.Type.EQ) {
            bind(query, counter, operation.column, argument(operation, 0));
            return;
        }

        if (type == Operation.Type.NOT) {
            bind(query, counter, operation.column, argument(operation, 0));
            return;
        }

        if (type == Operation.Type.IN) {
            bind(query, counter, operation.column, argument(operation, 0));
            return;
        }

        if (type == Operation.Type.NOT_IN) {
            bind(query, counter, operation.column, argument(operation, 0));
            return;
        }

        if (type == Operation.Type.BETWEEN) {
            bind(query, counter, operation.column, argument(operation, 0));
            bind(query, counter, operation.column, argument(operation, 1));
            return;
        }

        if (type == Operation.Type.OUTSIDE) {
            bind(query, counter, operation.column, argument(operation, 0));
            bind(query, counter, operation.column, argument(operation, 1));
            return;
        }

        if (type == Operation.Type.LIKE) {
            bind(query, counter, operation.column, '%' + argument(operation, 0).toString() + '%');
            return;
        }

        if (type == Operation.Type.NOT_LIKE) {
            bind(query, counter, operation.column, '%' + argument(operation, 0).toString() + '%');
            return;
        }

        throw new RepositoryQueryCompileException("Unknown operation type " + type.toString());
    }

    private void bind(TypedQuery<E> query, ParameterCounter parameterCounter, String parameter, Object value)
    {
        query.setParameter("parameter_" + parameterCounter.x++, value);
    }

    private String prefix(String column)
    {
        return c.getSimpleName() + '.' + column;
    }

    private String parameter(ParameterCounter parameterCounter)
    {
        return ":parameter_" + parameterCounter.x++;
    }

    private Object argument(Operation operation, int n)
    {
        if (n >= operation.arguments.length)
            throw new RepositoryQueryCompileException("Missing argument at index " + n);

        return operation.arguments[n];
    }

    private TypedQuery<E> createQuery()
    {
        TypedQuery<E> query = entityManager.createQuery(String.format("SELECT %s FROM %s %s " + generateJPQL(),
                                                                      c.getSimpleName(),
                                                                      c.getSimpleName(),
                                                                      c.getSimpleName()), c);
        bindJPQL(query);

        return query;
    }

    @Override
    public List<E> getAll()
    {
        TypedQuery<E> query = createQuery();

        return query.getResultList();
    }

    @Override
    public List<E> getPage(int pageSize, int pageNumber)
    {
        return null;
    }

    @Override
    public List<E> getFirst(int n)
    {
        return null;
    }

    @Override
    public E get(int n)
    {
        return null;
    }
}
