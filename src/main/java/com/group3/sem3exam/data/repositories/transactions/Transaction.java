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
    @Override public Transactional begin()
    {
        if (!this.currentEntityManager.getTransaction().isActive())
            this.currentEntityManager.getTransaction().begin();

        return this;
    }

    /**
     * Commits the current transaction.
     *
     * @return this
     */
    @Override public Transactional commit()
    {
        this.currentEntityManager.getTransaction().commit();

        return this;
    }

    /**
     * Rolls back the current transaction.
     *
     * @return this
     */
    @Override public Transactional rollback()
    {
        this.currentEntityManager.getTransaction().rollback();

        return this;
    }

    /**
     * Closes the transaction.
     *
     * @return this
     */
    @Override public Transactional close()
    {
        this.currentEntityManager.close();

        return this;
    }
}
