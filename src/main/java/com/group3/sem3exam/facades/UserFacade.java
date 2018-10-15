package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.TransactionalCityRepository;
import com.group3.sem3exam.data.repositories.TransactionalUserRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;
import com.group3.sem3exam.rest.exceptions.UserNotFoundException;

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

    private UserRepository userRepository;

    /**
     * Finds the user with the provided integer.
     *
     * @param id The id of the user to return.
     * @return The user with the provided id.
     * @throws UserNotFoundException When a user with the provided id does not exist.
     */
    public User get(Integer id) throws UserNotFoundException
    {
        return userRepository.get(id);
    }

    /**
     * Creates a user from the provided information.
     *
     * @param name        The name of the user to create.
     * @param email       The email of the user to create.
     * @param password    The password of the user to create.
     * @param city        The id of the city the user to create resides in.
     * @param gender      The gender of the user to create.
     * @param dateOfBirth The date of birth of the user to create.
     * @return The newly created user entity.
     * @throws CityNotFoundException When a city with the provided id does not exist.
     */
    public User createUser(String name, String email, String password, Integer city, Gender gender, LocalDate dateOfBirth)
    throws CityNotFoundException
    {
        Transaction transaction = new Transaction(emf);

        try {

            transaction.begin();
            TransactionalUserRepository tur = new TransactionalUserRepository(transaction);
            TransactionalCityRepository tcr = new TransactionalCityRepository(transaction);

            City retrievedCity = tcr.get(city);
            if (retrievedCity == null)
                throw new CityNotFoundException(city);

            User user = tur.createUser(name, email, password, retrievedCity, gender, dateOfBirth);
            transaction.commit();
            return user;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }
}
