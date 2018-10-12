package com.group3.sem3exam.data.repositories.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Transactional
{

    private final List<TransactionalRepository> repositories = new ArrayList<>();
    private final EntityManagerFactory          entityManagerFactory;
    private       EntityManager                 currentEntityManager;

    public Transaction(EntityManagerFactory entityManagerFactory, TransactionalRepository... repositories)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.currentEntityManager = entityManagerFactory.createEntityManager();
        for (TransactionalRepository repository : repositories) {
            this.repositories.add(repository);
            repository.setEntityManager(this.currentEntityManager);
        }
    }

    /**
     * Begins the transaction.
     *
     * @return this
     */
    @Override
    public void begin()
    {
        if (!this.currentEntityManager.getTransaction().isActive())
            this.currentEntityManager.getTransaction().begin();
    }

    /**
     * Commits the current transaction.
     *
     * @return this
     */
    @Override
    public void commit()
    {
        this.currentEntityManager.getTransaction().commit();
    }

    /**
     * Rolls back the current transaction.
     *
     * @return this
     */
    @Override
    public void rollback()
    {
        this.currentEntityManager.getTransaction().rollback();
    }

    /**
     * Closes the transaction.
     *
     * @return this
     */
    @Override
    public void close()
    {
        this.currentEntityManager.close();
    }
}
