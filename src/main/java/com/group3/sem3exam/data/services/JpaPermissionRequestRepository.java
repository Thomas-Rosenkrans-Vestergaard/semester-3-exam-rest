package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaPermissionRequestRepository extends JpaCrudRepository<PermissionRequest, String>
        implements PermissionRequestRepository
{

    /**
     * Creates a new {@link JpaPermissionRequestRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaPermissionRequestRepository(EntityManager entityManager)
    {
        super(entityManager, PermissionRequest.class);
    }

    /**
     * Creates a new {@link JpaPermissionRequestRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaPermissionRequestRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, PermissionRequest.class);
    }

    /**
     * Creates a new {@link JpaPermissionRequestRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaPermissionRequestRepository(JpaTransaction transaction)
    {
        super(transaction, PermissionRequest.class);
    }

    @Override
    public PermissionRequest updateStatus(PermissionRequest request, PermissionRequest.Status status)
    {
        request.setStatus(status);
        update(request);
        return request;
    }

    @Override
    public PermissionRequest create(User user, String callback, PermissionTemplate template)
    {
        PermissionRequest permissionRequest = new PermissionRequest(user, callback, template);
        getEntityManager().persist(permissionRequest);
        return permissionRequest;
    }
}
