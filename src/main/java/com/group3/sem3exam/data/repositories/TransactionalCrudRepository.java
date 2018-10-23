package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TransactionalCrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>>
        extends TransactionalReadCrudRepository<E, K>
        implements CrudRepository<E, K>
{

    /**
     * Whether or not the repository committed write operations to the database immediately.
     */
    protected boolean autoCommit = false;

    /**
     * Creates a new {@link TransactionalCrudRepository} using the provided entity manager.
     *
     * @param entityManager The entity manager to perform operations upon.
     * @param c             The type of entity the operations are performed upon.
     */
    public TransactionalCrudRepository(EntityManager entityManager, Class<E> c)
    {
        super(entityManager, c);
    }

    /**
     * Creates a new {@link TransactionalCrudRepository} using the provided entity manager factory.
     *
     * @param entityManagerFactory The entity manager factory used to create the entity manager to perform operations
     *                             upon.
     * @param c                    The type of entity the operations are performed upon.
     */
    public TransactionalCrudRepository(EntityManagerFactory entityManagerFactory, Class<E> c)
    {
        super(entityManagerFactory, c);
    }

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     * @param c           The type of entity the operations are performed upon.
     */
    public TransactionalCrudRepository(Transaction transaction, Class<E> c)
    {
        super(transaction, c);
    }

    /**
     * Forces the entity to update.
     *
     * @param entity The entity to update.
     * @return The updated entity.
     */
    @Override
    public E update(E entity)
    {
        EntityManager entityManager = this.getEntityManager();
        try {
            if (autoCommit)
                begin();
            return (E) entityManager.merge(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        } finally {
            if (autoCommit)
                commit();
        }
    }

    /**
     * Deletes the entity with the provided id.
     *
     * @param id The id of the entity to delete.
     * @return The deleted entity, or {@code null} when no entity was deleted.
     */
    @Override
    public E delete(K id)
    {
        EntityManager entityManager = this.getEntityManager();
        E             find          = entityManager.find(c, id);
        if (find == null)
            return null;

        try {
            if (autoCommit)
                begin();
            entityManager.remove(find);
            return find;
        } finally {
            if (autoCommit)
                commit();
        }
    }

    /**
     * Indicates to the repository that all write operations should be committed to the database immediately.
     *
     * @return this
     */
    @Override
    public void autoCommit()
    {
        this.autoCommit = true;
    }
}
