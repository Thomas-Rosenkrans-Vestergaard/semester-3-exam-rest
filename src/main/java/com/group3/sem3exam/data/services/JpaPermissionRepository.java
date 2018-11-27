package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.AbstractJpaRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

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
    public List<Permission> getPermissionsFor(Service service, User user)
    {
        List<List> lists = getEntityManager()
                .createQuery("SELECT pr.template.permissions FROM PermissionRequest pr " +
                             "WHERE pr.template.service = :service AND pr.user = :user", List.class)
                .setParameter("service", service)
                .setParameter("user", user)
                .getResultList();


        List collect = new ArrayList();
        for (List list : lists)
            collect.addAll(list);

        return (List<Permission>) collect;
    }
}
