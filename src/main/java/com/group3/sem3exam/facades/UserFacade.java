package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.TransactionalUserRepository;
import com.group3.sem3exam.rest.authentication.AuthenticationException;
import com.group3.sem3exam.rest.authentication.UserAuthenticator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class UserFacade
{
    private EntityManagerFactory emf;
    public UserFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public User createUser(String name, String email, String password)
    {
        TransactionalUserRepository tup = new TransactionalUserRepository(emf);
        return tup.createUser(name, email, password);

    }

    public User authenticate(String email, String password) throws AuthenticationException
    {
        TransactionalUserRepository tup = new TransactionalUserRepository(emf);
        UserAuthenticator uf = new UserAuthenticator(tup);

        return uf.authenticate(email, password);
    }
}
