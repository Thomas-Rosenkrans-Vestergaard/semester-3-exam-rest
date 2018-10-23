package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCityRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.rest.JpaConnection;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;
import com.group3.sem3exam.rest.exceptions.UserNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.function.Supplier;

public class UserFacade
{

    /**
     * The factory that produces user repositories used by this facade.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * Creates a new {@link UserFacade}.
     *
     * @param userRepositoryFactory The factory that produces user repositories used by this facade.
     */
    public UserFacade(Supplier<UserRepository> userRepositoryFactory)
    {
        this.userRepositoryFactory = userRepositoryFactory;
    }

    /**
     * Finds the user with the provided integer.
     *
     * @param id The id of the user to return.
     * @return The user with the provided id.
     * @throws UserNotFoundException When a user with the provided id does not exist.
     */
    public User get(Integer id) throws UserNotFoundException
    {
        try (UserRepository ur = userRepositoryFactory.get()) {
            User user = ur.get(id);
            if (user == null)
                throw new UserNotFoundException(id);

            return user;
        }
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
        try (JpaTransaction transaction = new JpaTransaction(JpaConnection.create())) {

            transaction.begin();
            JpaUserRepository tur = new JpaUserRepository(transaction);
            JpaCityRepository tcr = new JpaCityRepository(transaction);

            City retrievedCity = tcr.get(city);
            if (retrievedCity == null)
                throw new CityNotFoundException(city);

            User user = tur.createUser(name, email, hash(password), retrievedCity, gender, dateOfBirth);
            transaction.commit();
            return user;
        }
    }

    /**
     * Hashes the provided password using the bcrypt algorithm.
     *
     * @param password The password to hash.
     * @return The resulting hash.
     */
    private String hash(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
