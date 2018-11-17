package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.queries.JpaRepositoryQuery;
import com.group3.sem3exam.data.repositories.queries.RepositoryQuery;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of {@cide ReadCrudRepository}, implementing common read operations for some entity type using a
 * backing JPA data source.
 *
 * @param <E> The type of the entity managed by the repository.
 * @param <K> The type of the key of the entities managed by the repository.
 */
public class JpaReadCrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>>
        extends AbstractJpaRepository
        implements ReadCrudRepository<E, K>
{

    /**
     * The type of the key of the entity the operations are performed upon.
     */
    public final Class<K> kClass;

    /**
     * The attribute of the key of the entity the operations are performed upon.
     */
    public final String kAttribute;

    /**
     * The type of entity the operations are performed upon.
     */
    public final Class<E> eClass;

    /**
     * Creates a new {@link JpaReadCrudRepository} using the provided entity manager.
     *
     * @param entityManager The entity manager to perform operations upon.
     * @param kClass        The type of the key of the entity the operations are performed upon.
     * @param kAttribute    The attribute of the key of the entity the operations are performed upon.
     * @param eClass        The type of entity the operations are performed upon.
     */
    public JpaReadCrudRepository(EntityManager entityManager, Class<K> kClass, String kAttribute, Class<E> eClass)
    {
        super(entityManager);
        this.eClass = eClass;
        this.kClass = kClass;
        this.kAttribute = kAttribute;
    }

    /**
     * Creates a new {@link JpaReadCrudRepository} using the provided entity manager factory.
     *
     * @param entityManagerFactory The entity manager factory used to create the entity manager to perform operations
     *                             upon.
     * @param kClass               The type of the key of the entity the operations are performed upon.
     * @param kAttribute           The attribute of the key of the entity the operations are performed upon.
     * @param eClass               The type of entity the operations are performed upon.
     */
    public JpaReadCrudRepository(EntityManagerFactory entityManagerFactory, Class<K> kClass, String kAttribute, Class<E> eClass)
    {
        super(entityManagerFactory);
        this.eClass = eClass;
        this.kClass = kClass;
        this.kAttribute = kAttribute;
    }

    /**
     * Creates a new {@link JpaReadCrudRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     * @param kClass      The type of the key of the entity the operations are performed upon.
     * @param kAttribute  The attribute of the key of the entity the operations are performed upon.
     * @param eClass      The type of entity the operations are performed upon.
     */
    public JpaReadCrudRepository(JpaTransaction transaction, Class<K> kClass, String kAttribute, Class<E> eClass)
    {
        super(transaction);
        this.eClass = eClass;
        this.kClass = kClass;
        this.kAttribute = kAttribute;
    }

    /**
     * Returns all the entities in the repository.
     *
     * @return All the entities in the repository.
     */
    @Override
    public List<E> get()
    {
        return getEntityManager()
                .createQuery("SELECT e FROM " + eClass.getSimpleName() + " e", eClass)
                .getResultList();
    }

    /**
     * Returns a *page* of entities in the repository. The entities retrieved, in a zero-based manor, are defined
     * from <code> (pageNumber - 1) * pageSize</code> to <code>(pageSize) * perPage</code>.
     *
     * @param pageSize   The number of entities per page.
     * @param pageNumber The page number to getCities. Starts at 1.
     * @return The image on the selected page.
     */
    @Override
    public List<E> getPaginated(int pageSize, int pageNumber)
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        return getEntityManager()
                .createQuery("SELECT e FROM " + eClass.getSimpleName() + " e", eClass)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Returns the number of defined entities.
     *
     * @return The number of defined entities.
     */
    @Override
    public long count()
    {
        return getEntityManager()
                .createQuery("SELECT count(e) FROM " + eClass.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }

    /**
     * Returns the entity with the provided id.
     *
     * @param id The id of the entity to return.
     * @return The entity with the provided id, or {@code null} when no such entity exsits.
     */
    @Override
    public E get(K id)
    {
        return getEntityManager().find(eClass, id);
    }

    /**
     * Returns all the entities with the provided ids. When an entity with a provided id does not exist, the
     * {@code null} value is not inserted in the return map.
     *
     * @param ids The ids of entity to return.
     * @return The returned entities mapped to their id. The returned map is an HashMap, and the order of the returned
     * entities is not consistent.
     */
    @Override
    public Map<K, E> get(Set<K> ids)
    {
        List<E> results = getEntityManager()
                .createQuery("SELECT e FROM " + eClass.getSimpleName() + " e WHERE e.id IN :ids", eClass)
                .setParameter("ids", ids)
                .getResultList();

        Map<K, E> returnMap = new HashMap<>();
        for (E entity : results)
            returnMap.put(entity.getId(), entity);

        return returnMap;
    }

    /**
     * Checks whether or not an entity with the provided id exists.
     *
     * @param id The id to check for.
     * @return {@code true} when an entity with the provided id exists, {@code false} otherwise.
     */
    @Override
    public boolean exists(K id)
    {
        Long count = getEntityManager()
                .createQuery("SELECT count(e.id) FROM " + eClass.getSimpleName() + " e WHERE e.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return count > 0;
    }


    /**
     * Creates and returns a new {@link RepositoryQuery}.
     *
     * @return The query.
     */
    @Override
    public RepositoryQuery<K, E> query()
    {
        return new JpaRepositoryQuery<>(getEntityManager(), kClass, kAttribute, eClass);
    }
}
