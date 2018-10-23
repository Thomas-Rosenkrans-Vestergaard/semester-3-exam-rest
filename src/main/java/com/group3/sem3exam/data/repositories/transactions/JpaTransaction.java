package com.group3.sem3exam.data.repositories.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class JpaTransaction implements Transaction
{

    /**
     * The entity manager the transaction represents.
     */
    private EntityManager entityManager;

    /**
     * Creates a new {@link JpaTransaction} that represents the provided entity manager.
     *
     * @param entityManager The entity manager the transaction should represent.
     */
    public JpaTransaction(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    /**
     * Creates a new {@link JpaTransaction} that represents the provided entity manager.
     *
     * @param factory The entity manager factory that created the entity manager the transaction should represent.
     */
    public JpaTransaction(EntityManagerFactory factory)
    {
        this(factory.createEntityManager());
    }

    /**
     * Begins the transaction.
     */
    @Override
    public void begin()
    {
        EntityTransaction entityTransaction = this.entityManager.getTransaction();
        if (!entityTransaction.isActive())
            entityTransaction.begin();
    }

    /**
     * Commits the current transaction.
     */
    @Override
    public void commit()
    {
        EntityTransaction entityTransaction = this.entityManager.getTransaction();
        if (entityTransaction.isActive())
            entityTransaction.commit();
    }

    /**
     * Rolls back the current transaction.
     */
    @Override
    public void rollback()
    {
        EntityTransaction entityTransaction = this.entityManager.getTransaction();
        if (entityTransaction.isActive())
            entityTransaction.rollback();
    }

    /**
     * Closes the transaction. When the transaction is still active, meaning the result has not yet been committed or
     * rolled back, the transaction is rolled back.
     */
    @Override
    public void close()
    {
        EntityTransaction entityTransaction = this.entityManager.getTransaction();
        if (entityTransaction.isActive())
            entityTransaction.rollback();

        this.entityManager.close();
    }

    /**
     * Returns the current entity manager.
     *
     * @return The current entity manager.
     */
    public EntityManager getEntityManager()
    {
        return entityManager;
    }
}
