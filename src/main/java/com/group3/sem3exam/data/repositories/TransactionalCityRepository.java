package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TransactionalCityRepository extends TransactionalReadCrudRepository<City, Integer> implements CityRepository
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

    /**
     * Returns the cities in the region with the provided id.
     *
     * @param region The id of the region to return the cities of.
     * @return The cities in the region with the provided id.
     */
    @Override
    public List<City> getByRegion(int region)
    {
        return getEntityManager()
                .createQuery("SELECT c FROM City c WHERE c.region.id = :region")
                .setParameter("region", region)
                .getResultList();
    }
}
