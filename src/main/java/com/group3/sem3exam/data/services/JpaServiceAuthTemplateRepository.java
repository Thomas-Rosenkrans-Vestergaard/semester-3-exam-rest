package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class JpaServiceAuthTemplateRepository extends JpaCrudRepository<ServiceAuthTemplate, Integer>
        implements ServiceAuthTemplateRepository
{

    public JpaServiceAuthTemplateRepository(EntityManager entityManager, Class<ServiceAuthTemplate> c)
    {
        super(entityManager, c);
    }

    public JpaServiceAuthTemplateRepository(EntityManagerFactory entityManagerFactory, Class<ServiceAuthTemplate> c)
    {
        super(entityManagerFactory, c);
    }

    public JpaServiceAuthTemplateRepository(JpaTransaction transaction, Class<ServiceAuthTemplate> c)
    {
        super(transaction, c);
    }

    @Override
    public ServiceAuthTemplate create(String message, List<ServicePrivilege> privileges, Service service)
    {
        ServiceAuthTemplate template = new ServiceAuthTemplate(message, privileges, service);
        getEntityManager().persist(template);
        return template;
    }
}
