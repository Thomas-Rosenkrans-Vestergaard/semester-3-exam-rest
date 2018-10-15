package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.TransactionalUserRepository;
import jdk.vm.ci.meta.Local;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;

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
    public User createUser(String name, String email, String password, City city, Gender gender, LocalDate dateOfBirth)
    {
        try (TransactionalUserRepository tup = new TransactionalUserRepository(emf)) {
            tup.begin();
            User user = tup.createUser(name, email, password, city, gender, dateOfBirth);
            tup.commit();
            return user;
        }

    }
}
