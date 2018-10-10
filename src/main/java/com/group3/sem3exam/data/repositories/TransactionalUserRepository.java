package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

    @Override public User createUser(String name, String email, String password)
    {
        return null;
    }
}
