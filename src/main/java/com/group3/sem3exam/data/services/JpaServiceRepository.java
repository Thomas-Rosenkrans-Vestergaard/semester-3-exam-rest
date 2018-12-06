package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

import static com.group3.sem3exam.data.services.Service.Status.*;

public class JpaServiceRepository extends JpaCrudRepository<Service, Integer> implements ServiceRepository
{

    /**
     * Creates a new {@link JpaServiceRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaServiceRepository(EntityManager entityManager)
    {
        super(entityManager, Service.class);
    }

    /**
     * Creates a new {@link JpaServiceRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaServiceRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Service.class);
    }

    /**
     * Creates a new {@link JpaServiceRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaServiceRepository(JpaTransaction transaction)
    {
        super(transaction, Service.class);
    }

    @Override
    public Service create(String name, String passwordHash)
    {
        Service service = new Service(name, passwordHash, PENDING);
        getEntityManager().persist(service);
        return service;
    }

    @Override
    public Service getByName(String name)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT s FROM Service s WHERE s.name = :name", Service.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Service disable(Service service)
    {
        service.setStatus(DISABLED);
        return update(service);
    }

    @Override
    public Service enable(Service service)
    {
        service.setStatus(ENABLED);
        return update(service);
    }

    @Override
    public List<Service> searchPaginated(String name, int pageSize, int pageNumber)
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        return getEntityManager()
                .createQuery("SELECT s FROM Service s WHERE s.name LIKE :search")
                .setParameter("search", '%' + name + '%')
                .setFirstResult(pageSize * (pageNumber - 1))
                .setMaxResults(pageSize)
                .getResultList();
    }
}
