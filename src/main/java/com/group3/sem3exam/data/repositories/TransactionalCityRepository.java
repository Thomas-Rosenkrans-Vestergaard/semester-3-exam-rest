package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.repositories.transactions.AbstractTransactionalRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TransactionalCityRepository extends TransactionalCrudRepository<City, Integer> implements CityRepository
{

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public TransactionalCityRepository(EntityManager entityManager)
    {
        super(entityManager, City.class);
    }

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public TransactionalCityRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, City.class);
    }

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalCityRepository(Transaction transaction)
    {
        super(transaction, City.class);
    }
}
