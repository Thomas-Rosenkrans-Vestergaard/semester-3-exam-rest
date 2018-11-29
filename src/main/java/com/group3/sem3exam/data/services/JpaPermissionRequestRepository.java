package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaPermissionRequestRepository extends JpaCrudRepository<PermissionRequest, String>
        implements PermissionRequestRepository
{

    public JpaPermissionRequestRepository(EntityManager entityManager)
    {
        super(entityManager, PermissionRequest.class);
    }

    public JpaPermissionRequestRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, PermissionRequest.class);
    }

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
