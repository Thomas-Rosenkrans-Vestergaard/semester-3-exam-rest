package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static com.group3.sem3exam.data.services.ServicePrivilegeRequest.Status.ACCEPTED;
import static com.group3.sem3exam.data.services.ServicePrivilegeRequest.Status.REJECTED;

public class JpaServiceAuthRequestRepository extends JpaCrudRepository<ServicePrivilegeRequest, Integer>
        implements ServiceAuthRequestRepository
{

    /**
     * Creates a new {@link JpaServiceAuthRequestRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaServiceAuthRequestRepository(EntityManager entityManager)
    {
        super(entityManager, ServicePrivilegeRequest.class);
    }

    /**
     * Creates a new {@link JpaServiceAuthRequestRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaServiceAuthRequestRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, ServicePrivilegeRequest.class);
    }

    /**
     * Creates a new {@link JpaServiceAuthRequestRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaServiceAuthRequestRepository(JpaTransaction transaction)
    {
        super(transaction, ServicePrivilegeRequest.class);
    }

    @Override
    public ServiceAuthTemplate getDeclaration(Integer id)
    {
        return getEntityManager().find(ServiceAuthTemplate.class, id);
    }

    @Override
    public ServicePrivilegeRequest request(ServiceAuthTemplate template, User user)
    {
        ServicePrivilegeRequest toCreate = new ServicePrivilegeRequest(user, template);
        getEntityManager().persist(toCreate);
        return toCreate;
    }

    @Override
    public ServicePrivilegeRequest accept(ServicePrivilegeRequest request)
    {
        request.setStatus(ACCEPTED);
        return update(request);
    }

    @Override
    public ServicePrivilegeRequest reject(ServicePrivilegeRequest request)
    {
        request.setStatus(REJECTED);
        return update(request);
    }
}
