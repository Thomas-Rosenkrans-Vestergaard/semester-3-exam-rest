package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.entities.AuthRequest;
import com.group3.sem3exam.data.services.entities.PermissionTemplate;
import com.group3.sem3exam.data.services.entities.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaAuthRequestRepository extends JpaCrudRepository<AuthRequest, String> implements AuthRequestRepository
{

    /**
     * Creates a new {@link JpaAuthRequestRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaAuthRequestRepository(EntityManager entityManager)
    {
        super(entityManager, AuthRequest.class);
    }

    /**
     * Creates a new {@link JpaAuthRequestRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaAuthRequestRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, AuthRequest.class);
    }

    /**
     * Creates a new {@link JpaAuthRequestRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaAuthRequestRepository(JpaTransaction transaction)
    {
        super(transaction, AuthRequest.class);
    }

    @Override
    public AuthRequest create(Service service, String callback, PermissionTemplate template)
    {
        AuthRequest authRequest = new AuthRequest(service, callback, template);
        getEntityManager().persist(authRequest);
        return authRequest;
    }

    @Override
    public void completed(AuthRequest authRequest)
    {
        authRequest.setStatus(AuthRequest.Status.COMPLETED);
    }
}
