package com.group3.sem3exam.data.repositories.base;

import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@code ReadRepository}, implementing common read and write operations for some entity type
 * using a backing JPA data source.
 *
 * @param <E> The type of the entity managed by the repository.
 * @param <K> The type of the key of the entities managed by the repository.
 */
public abstract class JpaCrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>>
        extends JpaReadRepository<E, K>
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
     * Creates a new {@link JpaCrudRepository}.
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
        E             find          = entityManager.find(c, id);
        if (find == null)
            return null;

        entityManager.remove(find);
        return find;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity The entity to delete.
     * @return The deleted entity.
     */
    @Override
    public E delete(E entity)
    {
        return delete(entity.getId());
    }

    /**
     * Deletes the entities with keys matching one of the provided keys.
     *
     * @param keys The keys of the entities to delete.
     * @return The map of the deleted entities. The key of the deleted entity is mapped to its id.
     */
    @Override
    public Map<K, E> delete(Collection<K> keys)
    {
        Map<K, E> results = new HashMap<>(keys.size());
        for (K key : keys) {
            E deleted = delete(key);
            results.put(key, deleted);
        }

        return results;
    }

    /**
     * Deletes all the provided entities.
     * <p>
     * The provided list instance is the same as the returned instance.
     *
     * @param entities The entities to delete from the repository.
     * @return A list with the updated entities.
     */
    @Override
    public List<E> delete(List<E> entities)
    {
        for (E entity : entities)
            delete(entity);

        return entities;
    }
}
