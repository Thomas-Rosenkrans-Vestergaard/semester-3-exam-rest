package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class JpaPermissionTemplateRepository extends JpaCrudRepository<PermissionTemplate, String>
        implements PermissionTemplateRepository
{

    public JpaPermissionTemplateRepository(EntityManager entityManager)
    {
        super(entityManager, PermissionTemplate.class);
    }

    public JpaPermissionTemplateRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, PermissionTemplate.class);
    }

    public JpaPermissionTemplateRepository(JpaTransaction transaction)
    {
        super(transaction, PermissionTemplate.class);
    }

    @Override
    public PermissionTemplate create(String message, List<Permission> permissions, Service service)
    {
        PermissionTemplate template = new PermissionTemplate(message, permissions, service);
        getEntityManager().persist(template);
        return template;
    }

    @Override
    public List<PermissionTemplate> getByService(Service service)
    {
        return getEntityManager()
                .createQuery("SELECT pt FROM PermissionTemplate pt WHERE pt.service = :service")
                .setParameter("service", service)
                .getResultList();
    }
}
