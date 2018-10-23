package com.group3.sem3exam.data.repositories.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class AbstractTransactionalRepository implements TransactionalRepository
{

    /**
     * The entity manager that operations are performed upon.
     */
    private EntityManager entityManager;

    /**
     * Creates a new {@link AbstractTransactionalRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public AbstractTransactionalRepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    /**
     * Creates a new {@link AbstractTransactionalRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public AbstractTransactionalRepository(EntityManagerFactory entityManagerFactory)
    {
        this(entityManagerFactory.createEntityManager());
    }

    /**
     * Creates a new {@link AbstractTransactionalRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public AbstractTransactionalRepository(Transaction transaction)
    {
        this(transaction.getEntityManager());
    }

    /**
     * Begins the transaction.
     */
    @Override
    public void begin()
    {
        if (!this.entityManager.getTransaction().isActive())
            this.entityManager.getTransaction().begin();
    }

    /**
     * Closes the transaction. When the transaction is still active, meaning the result has not yet been committed or
     * rolled back, the transaction is rolled back.
     */
    @Override
    public void close()
    {
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.isActive())
            transaction.rollback();

        entityManager.close();
    }

    /**
     * Commits the current transaction.
     */
    @Override
    public void commit()
    {
        entityManager.getTransaction().commit();
    }

    /**
     * Rolls back the current transaction.
     */
    @Override
    public void rollback()
    {
        entityManager.getTransaction().rollback();
    }

    /**
     * Returns the currently active entity manager instance.
     *
     * @return The currently active entity manager.
     */
    @Override
    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    /**
     * Sets the currently active entity manager instance.
     *
     * @param entityManger The new active manager.
     */
    @Override
    public void setEntityManager(EntityManager entityManger)
    {
        this.entityManager = entityManager;
    }
}
