package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDate;

public interface UserRepository extends CrudRepository<User, Integer>
{

    /**
     * Inserts a new user into the repository with the provided information.
     *
     * @param name         The name of the new user.
     * @param email        The email of the new user.
     * @param passwordHash The password hash of the new user.
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


}
