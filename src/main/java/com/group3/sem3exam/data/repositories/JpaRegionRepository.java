package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class JpaRegionRepository extends JpaReadCrudRepository<Region, Integer> implements RegionRepository
{

    public JpaRegionRepository(EntityManager entityManager)
    {
        super(entityManager, Region.class);
    }

    /**
     * Creates a new {@link JpaRegionRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaRegionRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Region.class);
    }

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaRegionRepository(JpaTransaction transaction)
    {
        super(transaction, Region.class);
    }

    /**
     * Returns the regions in the country with the provide id.
     *
     * @param country The id of the country to return the regions of.
     * @return The regions in the country with the provided id.
     */
    @Override
    public List<Region> getByCountry(int country)
    {
        return getEntityManager()
                .createQuery("SELECT r FROM Region r WHERE r.country.id = :country", Region.class)
                .setParameter("country", country)
                .getResultList();
    }
}
