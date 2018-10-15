package com.group3.sem3exam.data.repositories.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Supplier;

public class Transaction implements Transactional
{

    /**
     * The entity manager the transaction represents.
     */
    private EntityManager entityManager;

    /**
     * Creates a new {@link Transaction} that represents the provided entity manager.
     *
     * @param entityManager The entity manager the transaction should represent.
     */
    public Transaction(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    /**
     * Creates a new {@link Transaction} that represents the provided entity manager.
     *
     * @param factory The entity manager factory that created the entity manager the transaction should represent.
     */
    public Transaction(EntityManagerFactory factory)
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
     * Closes the transaction.
     */
    @Override
    public void close()
    {
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
