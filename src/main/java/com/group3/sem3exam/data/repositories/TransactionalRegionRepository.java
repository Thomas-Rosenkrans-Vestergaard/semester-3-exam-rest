package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TransactionalRegionRepository extends TransactionalCrudRepository<Region, Integer> implements RegionRepository
{

    public TransactionalRegionRepository(EntityManager entityManager)
    {
        super(entityManager, Region.class);
    }

    /**
     * Creates a new {@link TransactionalRegionRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public TransactionalRegionRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Region.class);
    }

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalRegionRepository(Transaction transaction)
    {
        super(transaction, Region.class);
    }
}
