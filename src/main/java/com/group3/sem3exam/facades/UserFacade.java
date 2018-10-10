package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.TransactionalUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class UserFacade
{
    private EntityManager em;
    public UserFacade(EntityManagerFactory emf) {
        em = emf.createEntityManager();
    }

    public User createUser(String name, String email, String password)
    {
        TransactionalUserRepository tup = new TransactionalUserRepository(em);
        return tup.createUser(name, email, password);

    }
}
