package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TransactionalCountryRepository extends TransactionalReadCrudRepository<Country, Integer>
        implements CountryRepository
{

    /**
     * Creates a new {@link TransactionalCountryRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public TransactionalCountryRepository(EntityManager entityManager)
    {
        super(entityManager, Country.class);
    }

    /**
     * Creates a new {@link TransactionalCountryRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public TransactionalCountryRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Country.class);
    }

    /**
     * Creates a new {@link TransactionalCountryRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalCountryRepository(Transaction transaction)
    {
        super(transaction, Country.class);
    }

    /**
     * Returns a complete list of the countries in the repository.
     *
     * @return The complete list of the countries in the repository.
     */
    @Override
    public List<Country> get()
    {
        return getEntityManager()
                .createQuery("SELECT c FROM Country c", Country.class)
                .getResultList();
    }
}
