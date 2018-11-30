package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.AbstractJpaRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpaPermissionRepository extends AbstractJpaRepository implements PermissionRepository
{

    public JpaPermissionRepository(EntityManager entityManager)
    {
        super(entityManager);
    }

    public JpaPermissionRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory);
    }

    public JpaPermissionRepository(JpaTransaction transaction)
    {
        super(transaction);
    }

    @Override
    public Set<Permission> getPermissionsFor(Service service, User user)
    {
        List<Permission> permissions = getEntityManager()
                .createQuery("SELECT pm.permission FROM PermissionMapping pm " +
                             "WHERE pm.template IN (" +
                             "  SELECT pr.template FROM PermissionRequest pr " +
                             "  WHERE pr.user = :user AND pr.template.service = :service AND pr.status = :status" +
                             ")", Permission.class)
                .setParameter("user", user)
                .setParameter("service", service)
                .setParameter("status", PermissionRequest.Status.ACCEPTED)
                .getResultList();

        return new HashSet<>(permissions);
    }

    @Override
    public void setLastUpdated(Service service, User user, LocalDateTime time)
    {
        if (internalGet(service, user) == null)
            insert(service, user, time);
        else
            setLastUpdated(service, user, time);
    }

    @Override
    public LocalDateTime getLastUpdated(Service service, User user)
    {
        LocalDateTime fetched = internalGet(service, user);

        return fetched == null ? LocalDateTime.now() : fetched;
    }

    private LocalDateTime internalGet(Service service, User user)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT pu.time FROM PermissionUpdated pu " +
                                 "WHERE pu.user = :user AND pu.service = :service", LocalDateTime.class)
                    .setParameter("user", user)
                    .setParameter("service", service)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private void insert(Service service, User user, LocalDateTime time)
    {

    }
}
