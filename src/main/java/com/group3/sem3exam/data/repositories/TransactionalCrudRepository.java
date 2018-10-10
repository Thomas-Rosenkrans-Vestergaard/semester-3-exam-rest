package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.transactions.AbstractTransactionalRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TransactionalCrudRepository<E, ID> extends AbstractTransactionalRepository implements CrudRepository<E, ID>
{

    private Class<E> c;

    public TransactionalCrudRepository(EntityManager entityManager, Class<E> c)
    {
        super(entityManager);
        this.c = c;
    }

    public TransactionalCrudRepository(EntityManagerFactory entityManagerFactory, Class<E> c)
    {
        super(entityManagerFactory);
        this.c = c;
    }

    public TransactionalCrudRepository(Class<E> c)
    {
        super();
        this.c = c;
    }

    public List<E> get()
    {
        EntityManager entityManager = getEntityManager();

        return entityManager.createQuery("SELECT e FROM " + c.getSimpleName() + " e", c)
                            .getResultList();
    }

    @Override public List<E> get(int pageSize, int pageNumber)
    {
        return getEntityManager().createQuery("SELECT e FROM " + c.getSimpleName() + " e", c)
                                 .setFirstResult((pageNumber - 1) * pageSize)
                                 .setMaxResults(pageSize)
                                 .getResultList();
    }

    @Override public long count()
    {
        EntityManager entityManager = getEntityManager();

        return entityManager.createQuery("SELECT count(e) FROM " + c.getSimpleName() + " e", Long.class)
                            .getSingleResult();
    }

    public E get(ID id)
    {
        EntityManager entityManager = getEntityManager();

        return entityManager.find(c, id);
    }

    public E persist(E entity)
    {
        EntityManager entityManager = this.getEntityManager();
        entityManager.persist(entity);
        return entity;
    }

    public E update(E entity)
    {
        EntityManager entityManager = this.getEntityManager();
        return (E) entityManager.merge(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    public E delete(ID id)
    {
        EntityManager entityManager = this.getEntityManager();
        E             find          = entityManager.find(c, id);
        if (find == null)
            return null;

        entityManager.remove(find);
        return find;
    }
}
