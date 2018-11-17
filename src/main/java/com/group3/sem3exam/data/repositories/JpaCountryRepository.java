package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * An implementation of the {@code CountryRepository} interface, backed by a JPA data source.
 */
public class JpaCountryRepository extends JpaReadCrudRepository<Country, Integer>
        implements CountryRepository
{

    /**
     * Creates a new {@link JpaCountryRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaCountryRepository(EntityManager entityManager)
    {
        super(entityManager, Integer.class, "id", Country.class);
    }

    /**
     * Creates a new {@link JpaCountryRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaCountryRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Integer.class, "id", Country.class);
    }

    /**
     * Creates a new {@link JpaCountryRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaCountryRepository(JpaTransaction transaction)
    {
        super(transaction, Integer.class, "id", Country.class);
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
