package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.transactions.AbstractTransactionalRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TransactionalReadCrudRepository<E, ID> extends AbstractTransactionalRepository implements ReadCrudRepository<E, ID>
{

    /**
     * The type of entity the operations are performed upon.
     */
    protected final Class<E> c;

    /**
     * Creates a new {@link TransactionalReadCrudRepository} using the provided entity manager.
     *
     * @param entityManager The entity manager to perform operations upon.
     * @param c             The type of entity the operations are performed upon.
     */
    public TransactionalReadCrudRepository(EntityManager entityManager, Class<E> c)
    {
        super(entityManager);
        this.c = c;
    }

    /**
     * Creates a new {@link TransactionalReadCrudRepository} using the provided entity manager factory.
     *
     * @param entityManagerFactory The entity manager factory used to create the entity manager to perform operations
     *                             upon.
     * @param c                    The type of entity the operations are performed upon.
     */
    public TransactionalReadCrudRepository(EntityManagerFactory entityManagerFactory, Class<E> c)
    {
        super(entityManagerFactory);
        this.c = c;
    }

    /**
     * Creates a new {@link TransactionalReadCrudRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     * @param c           The type of entity the operations are performed upon.
     */
    public TransactionalReadCrudRepository(Transaction transaction, Class<E> c)
    {
        super(transaction);
        this.c = c;
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
                .createQuery("SELECT e FROM " + c.getSimpleName() + " e", c)
                .getResultList();
    }

    /**
     * Returns a *page* of entities in the repository. The entities retrieved, in a zero-based manor, are defined
     * from <code> (pageNumber - 1) * pageSize</code> to <code>(pageSize) * perPage</code>.
     *
     * @param pageSize   The number of entities per page.
     * @param pageNumber The page number to getCities. Starts at 1.
     * @return
     */
    @Override
    public List<E> getPaginated(int pageSize, int pageNumber)
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        return getEntityManager()
                .createQuery("SELECT e FROM " + c.getSimpleName() + " e", c)
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
                .createQuery("SELECT count(e) FROM " + c.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }

    /**
     * Returns the entity with the provided id.
     *
     * @param id The id of the entity to return.
     * @return The entity with the provided id, or {@code null} when no such entity exsits.
     */
    @Override
    public E get(ID id)
    {
        return getEntityManager().find(c, id);
    }

    /**
     * Checks whether or not an entity with the provided id exists.
     *
     * @param id The id to check for.
     * @return {@code true} when an entity with the provided id exists, {@code false} otherwise.
     */
    @Override
    public boolean exists(ID id)
    {
        Long count = getEntityManager()
                .createQuery("SELECT count(e.id) FROM " + c.getSimpleName() + " e WHERE e.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return count > 0;
    }
}
