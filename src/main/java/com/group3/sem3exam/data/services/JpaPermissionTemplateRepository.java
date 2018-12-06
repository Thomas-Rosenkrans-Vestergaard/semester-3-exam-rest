package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.ArrayList;
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
    public PermissionTemplate create(String name, String message, List<Permission> permissions, Service service)
    {
        PermissionTemplate      template = new PermissionTemplate(name, message, null, service);
        List<PermissionMapping> mappings = new ArrayList<>();
        for (Permission permission : permissions)
            mappings.add(new PermissionMapping(template, permission));

        template.setPermissions(mappings);
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

    @Override
    public PermissionTemplate getByName(Service service, String name)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT pt FROM PermissionTemplate pt " +
                                 "WHERE pt.service = :service AND pt.name = :name", PermissionTemplate.class)
                    .setParameter("service", service)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
