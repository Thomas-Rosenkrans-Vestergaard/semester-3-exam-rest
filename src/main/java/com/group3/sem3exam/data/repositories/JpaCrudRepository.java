package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * An implementation of {@cide ReadCrudRepository}, implementing common read and write operations for some entity type
 * using a backing JPA data source.
 *
 * @param <E> The type of the entity managed by the repository.
 * @param <K> The type of the key of the entities managed by the repository.
 */
public class JpaCrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>>
        extends JpaReadCrudRepository<E, K>
        implements CrudRepository<E, K>
{

    /**
     * Creates a new {@link JpaCrudRepository} using the provided entity manager.
     *
     * @param entityManager The entity manager to perform operations upon.
     * @param kClass        The type of the key of the entity the operations are performed upon.
     * @param kAttribute    The attribute of the key of the entity the operations are performed upon.
     * @param eClass        The type of entity the operations are performed upon.
     */
    public JpaCrudRepository(EntityManager entityManager, Class<K> kClass, String kAttribute, Class<E> eClass)
    {
        super(entityManager, kClass, kAttribute, eClass);
    }

    /**
     * Creates a new {@link JpaCrudRepository} using the provided entity manager factory.
     *
     * @param entityManagerFactory The entity manager factory used to create the entity manager to perform operations
     *                             upon.
     * @param kClass               The type of the key of the entity the operations are performed upon.
     * @param kAttribute           The attribute of the key of the entity the operations are performed upon.
     * @param eClass               The type of entity the operations are performed upon.
     */
    public JpaCrudRepository(EntityManagerFactory entityManagerFactory, Class<K> kClass, String kAttribute, Class<E> eClass)
    {
        super(entityManagerFactory, kClass, kAttribute, eClass);
    }

    /**
     * Creates a new {@link JpaCrudRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     * @param kClass      The type of the key of the entity the operations are performed upon.
     * @param kAttribute  The attribute of the key of the entity the operations are performed upon.
     * @param eClass      The type of entity the operations are performed upon.
     */
    public JpaCrudRepository(JpaTransaction transaction, Class<K> kClass, String kAttribute, Class<E> eClass)
    {
        super(transaction, kClass, kAttribute, eClass);
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
        return (E) entityManager.merge(entityManager.contains(entity) ? entity : entityManager.merge(entity));
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
        E             find          = entityManager.find(eClass, id);
        if (find == null)
            return null;

        entityManager.remove(find);
        return find;
    }
}
