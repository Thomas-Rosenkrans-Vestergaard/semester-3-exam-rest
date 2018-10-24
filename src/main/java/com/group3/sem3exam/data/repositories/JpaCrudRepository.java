package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaCrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>>
        extends JpaReadCrudRepository<E, K>
        implements CrudRepository<E, K>
{

    /**
     * Creates a new {@link JpaCrudRepository} using the provided entity manager.
     *
     * @param entityManager The entity manager to perform operations upon.
     * @param c             The type of entity the operations are performed upon.
     */
    public JpaCrudRepository(EntityManager entityManager, Class<E> c)
    {
        super(entityManager, c);
    }

    /**
     * Creates a new {@link JpaCrudRepository} using the provided entity manager factory.
     *
     * @param entityManagerFactory The entity manager factory used to create the entity manager to perform operations
     *                             upon.
     * @param c                    The type of entity the operations are performed upon.
     */
    public JpaCrudRepository(EntityManagerFactory entityManagerFactory, Class<E> c)
    {
        super(entityManagerFactory, c);
    }

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     * @param c           The type of entity the operations are performed upon.
     */
    public JpaCrudRepository(JpaTransaction transaction, Class<E> c)
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
}