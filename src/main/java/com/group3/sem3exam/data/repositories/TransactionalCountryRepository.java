package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TransactionalCountryRepository extends TransactionalCrudRepository<Country, Integer> implements CountryRepository
{

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public TransactionalCountryRepository(EntityManager entityManager)
    {
        super(entityManager, Country.class);
    }

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public TransactionalCountryRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Country.class);
    }

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalCountryRepository(Transaction transaction)
    {
        super(transaction, Country.class);
    }
}
