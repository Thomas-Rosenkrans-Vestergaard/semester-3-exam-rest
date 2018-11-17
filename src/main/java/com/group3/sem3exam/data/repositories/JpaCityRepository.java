package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * An implementation of the {@code CityRepository} interface, backed by a JPA data source.
 */
public class JpaCityRepository extends JpaReadCrudRepository<City, Integer> implements CityRepository
{

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaCityRepository(EntityManager entityManager)
    {
        super(entityManager, Integer.class, "id", City.class);
    }

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaCityRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Integer.class, "id", City.class);
    }

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaCityRepository(JpaTransaction transaction)
    {
        super(transaction, Integer.class, "id", City.class);
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
