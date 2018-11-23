package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.*;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static com.group3.sem3exam.data.entities.ServiceRequest.Status.ACCEPTED;
import static com.group3.sem3exam.data.entities.ServiceRequest.Status.REJECTED;

public class JpaServiceRequestRepository
        extends JpaCrudRepository<ServiceRequest, Integer>
        implements ServiceRequestRepository
{

    /**
     * Creates a new {@link JpaServiceRequestRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaServiceRequestRepository(EntityManager entityManager)
    {
        super(entityManager, ServiceRequest.class);
    }

    /**
     * Creates a new {@link JpaServiceRequestRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaServiceRequestRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, ServiceRequest.class);
    }

    /**
     * Creates a new {@link JpaServiceRequestRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaServiceRequestRepository(JpaTransaction transaction)
    {
        super(transaction, ServiceRequest.class);
    }

    @Override
    public ServiceRequestTemplate declare(String message, List<ServicePrivilege> privileges, Service service)
    {
        ServiceRequestTemplate toCreate = new ServiceRequestTemplate(message, privileges, service);
        getEntityManager().persist(toCreate);
        return toCreate;
    }

    @Override
    public ServiceRequestTemplate getDeclaration(Integer id)
    {
        return getEntityManager().find(ServiceRequestTemplate.class, id);
    }

    @Override
    public ServiceRequest request(ServiceRequestTemplate template, User user)
    {
        ServiceRequest toCreate = new ServiceRequest(user, template);
        getEntityManager().persist(toCreate);
        return toCreate;
    }

    @Override
    public ServiceRequest accept(ServiceRequest request)
    {
        request.setStatus(ACCEPTED);
        return update(request);
    }

    @Override
    public ServiceRequest reject(ServiceRequest request)
    {
        request.setStatus(REJECTED);
        return update(request);
    }
}
