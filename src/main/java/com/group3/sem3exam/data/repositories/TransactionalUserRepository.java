package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class TransactionalUserRepository extends TransactionalCrudRepository<User, Integer> implements UserRepository
{

    public TransactionalUserRepository(EntityManager entityManager)
    {
        super(entityManager, User.class);
    }

    public TransactionalUserRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, User.class);
    }

    public TransactionalUserRepository()
    {
        super(User.class);
    }

    @Override
    public User createUser(String name, String email, String passwordHash)
    {
        User user = new User(name, email, passwordHash);

        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(user);
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
        } finally {
            getEntityManager().close();
            return user;
        }
    }

    @Override
    public User withEmail(String email)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
