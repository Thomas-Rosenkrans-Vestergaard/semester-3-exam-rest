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
    @Override public Transactional begin()
    {
        this.entityManager.getTransaction().begin();

        return this;
    }

    /**
     * Closes the transaction.
     *
     * @return this
     */
    @Override public Transactional close()
    {
        entityManager.close();

        return this;
    }

    @Override public EntityManager getEntityManager()
    {
        return entityManager;
    }

    @Override public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override public AbstractTransactionalRepository commit()
    {
        entityManager.getTransaction().commit();

        return this;
    }

    @Override public AbstractTransactionalRepository rollback()
    {
        entityManager.getTransaction().rollback();

        return this;
    }
}
