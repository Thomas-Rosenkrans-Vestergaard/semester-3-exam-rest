package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.TransactionalUserRepository;
import com.group3.sem3exam.rest.authentication.AuthenticationException;
import com.group3.sem3exam.rest.authentication.UserAuthenticator;

import javax.persistence.EntityManagerFactory;


public class UserFacade
{

    /**
     * The entity manager factory used to access the database.
     */
    private EntityManagerFactory emf;

    /**
     * Creates a new {@link UserFacade}.
     *
     * @param emf The entity manager factory used to access the database.
     */
    public UserFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    /**
     * Creates a user from the provided information.
     *
     * @param name     The name of the user to create.
     * @param email    The email of the user to create.
     * @param password The password of the user to create.
     * @return The newly created user entity.
     */
    public User createUser(String name, String email, String password)
    {
        try (TransactionalUserRepository tup = new TransactionalUserRepository(emf)) {
            return tup.createUser(name, email, password);
        }
    }

    /**
     * Attempts to authenticate a user with the provided credentials.
     *
     * @param email    The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The user entity who was authenticated.
     * @throws AuthenticationException When the credentials could not be used to authenticate the credentials.
     */
    public User authenticate(String email, String password) throws AuthenticationException
    {
        try (TransactionalUserRepository tup = new TransactionalUserRepository(emf)) {
            UserAuthenticator authenticator = new UserAuthenticator(tup);
            return authenticator.authenticate(email, password);
        }
    }
}
