package com.group3.sem3exam.data.repositories.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class AbstractTransactionalRepository implements TransactionalRepository
{

    private EntityManager entityManager;

    public AbstractTransactionalRepository()
    {

    }

    public AbstractTransactionalRepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public AbstractTransactionalRepository(EntityManagerFactory entityManagerFactory)
    {
        this(entityManagerFactory.createEntityManager());
    }

    /**
     * Begins the transaction.
     *
     * @return this
     */
    @Override
    public void begin()
    {
        this.entityManager.getTransaction().begin();
    }

    /**
     * Closes the transaction.
     */
    @Override
    public void close()
    {
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.isActive())
            transaction.rollback();

        entityManager.close();
    }

    @Override
    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    @Override
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public void commit()
    {
        entityManager.getTransaction().commit();
    }

    @Override
    public void rollback()
    {
        entityManager.getTransaction().rollback();
    }
}
