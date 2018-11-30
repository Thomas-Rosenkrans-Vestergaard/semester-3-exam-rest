package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.CrudRepository;
import com.group3.sem3exam.data.repositories.transactions.TransactionalRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a data source of users. Defines read and write operations on the data source.
 */
public interface UserRepository extends CrudRepository<User, Integer>, TransactionalRepository
{

    /**
     * Inserts a new user into the repository with the provided information.
     *
     * @param name         The name of the new user.
     * @param email        The email of the new user.
     * @param passwordHash The password hash of the new user.
     * @param city         The city the new user resides in.
     * @param gender       The gender of the new user.
     * @param dateOfBirth  The date of birth of the new user.
     * @return The resulting new user.
     */
    User createUser(String name, String email, String passwordHash, City city, Gender gender, LocalDate dateOfBirth);

    /**
     * Returns the user with the provided email.
     *
     * @param email The email fo the user to find and return.
     * @return The user with the provided email, {@code null} when no such user exists.
     */
    User getByEmail(String email);

    /**
     * Updates the profile picture of the provided user to the provided profile picture.
     *
     * @param user      The user to update the profile picture of.
     * @param full      The src of the new profile picture.
     * @param thumbnail The thumbnail of the new profile picture.
     * @return The updated user entity.
     */

    List<User> searchUsers(String input);

    User updateProfilePicture(User user, String full, String thumbnail);
}
