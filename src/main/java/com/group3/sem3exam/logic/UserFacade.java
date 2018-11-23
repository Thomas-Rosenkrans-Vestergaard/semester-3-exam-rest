package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.*;
import com.group3.sem3exam.data.repositories.CityRepository;
import com.group3.sem3exam.data.repositories.FriendshipRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.validation.ResourceValidationException;
import com.group3.sem3exam.logic.validation.ResourceValidator;
import com.group3.sem3exam.logic.validation.IsAfterCheck;
import com.group3.sem3exam.logic.validation.IsBeforeCheck;
import com.group3.sem3exam.rest.UserResource;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class UserFacade<T extends Transaction>
{

    /**
     * The factory that produces transactions used by this facade.
     */
    private final Supplier<T> transactionFactory;

    /**
     * The factory that produces user repositories used by this facade.
     */
    private final Function<T, UserRepository> userRepositoryFactory;

    /**
     * The factory that produces city repositories used by this facade.
     */
    private final Function<T, CityRepository> cityRepositoryFactory;

    /**
     * Creates a new {@link UserFacade}.
     *
     * @param transactionFactory    The factory that produces transactions used by this facade.
     * @param userRepositoryFactory The factory that produces user repositories used by this facade.
     * @param cityRepositoryFactory The factory that produces city repositories used by this facade.
     */
    public UserFacade(
            Supplier<T> transactionFactory,
            Function<T, UserRepository> userRepositoryFactory,
            Function<T, CityRepository> cityRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.cityRepositoryFactory = cityRepositoryFactory;
    }

    /**
     * Finds the user with the provided integer.
     *
     * @param id The id of the user to return.
     * @return The user with the provided id.
     * @throws ResourceNotFoundException When a user with the provided id does not exist.
     */
    public User get(Integer id) throws ResourceNotFoundException
    {
        try (UserRepository ur = userRepositoryFactory.apply(transactionFactory.get())) {
            User user = ur.get(id);
            if (user == null)
                throw new ResourceNotFoundException(User.class, id);

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
     * @throws ResourceNotFoundException When a city with the provided id does not exist.
     */
    public User createUser(String name, String email, String password, Integer city, Gender gender, LocalDate dateOfBirth)
    throws ResourceNotFoundException,
           ResourceValidationException
    {
        validate(name, email, password, city, gender, dateOfBirth);

        try (T transaction = transactionFactory.get()) {

            transaction.begin();
            UserRepository ur = userRepositoryFactory.apply(transaction);
            CityRepository cr = cityRepositoryFactory.apply(transaction);

            City retrievedCity = cr.get(city);
            if (retrievedCity == null)
                throw new ResourceNotFoundException(City.class, city);

            User user = ur.createUser(name, email, hash(password), retrievedCity, gender, dateOfBirth);
            transaction.commit();
            return user;
        }
    }

    /**
     * Validates that the provided information is valid as a user.
     *
     * @param name        The name of the user.
     * @param email       The email of the user.
     * @param password    The password of the user.
     * @param city        The city of the user.
     * @param gender      The gender of the user.
     * @param dateOfBirth The date of birth of the user.
     * @throws ResourceValidationException When the user could not be validated.
     */
    private void validate(String name, String email, String password, Integer city, Gender gender, LocalDate dateOfBirth)
    throws ResourceValidationException
    {

        UserValidator userValidator = new UserValidator();
        userValidator.name = name;
        userValidator.email = email;
        userValidator.password = password;
        userValidator.city = city;
        userValidator.gender = gender;
        userValidator.dateOfBirth = dateOfBirth;

        ResourceValidator<UserValidator> resourceValidator = new ResourceValidator<>(userValidator);
        resourceValidator.oval();
        resourceValidator.on("dateOfBirth", Temporal.class)
                         .check(IsAfterCheck.constructor(LocalDate.now().minusYears(120)))
                         .check(IsBeforeCheck.constructor(LocalDate.now()));
        if (resourceValidator.hasErrors())
            resourceValidator.throwResourceValidationException();
    }

    private static class UserValidator
    {
        @NotNull
        @Length(min = 1, max = 255)
        public String name;

        @NotNull
        @Email
        @Length(min = 1, max = 255)
        public String email;

        @NotNull
        @Length(min = 4)
        public String password;

        @NotNull
        public Integer city;

        @NotNull
        public Gender gender;

        @NotNull
        public LocalDate dateOfBirth;
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
