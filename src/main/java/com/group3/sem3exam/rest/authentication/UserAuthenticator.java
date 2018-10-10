package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserAuthenticator
{

    /**
     * The repository that users are retrieved from.
     */
    private final UserRepository repository;

    /**
     * Creates a new {@link UserAuthenticator}.
     *
     * @param repository The repository that users are retrieved from.
     */
    public UserAuthenticator(UserRepository repository)
    {
        this.repository = repository;
    }

    /**
     * Attempts to authenticate using the provided user credentials.
     *
     * @param email    The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The user who was authenticated, {@code null} when the user could not be authenticated.
     * @throws AuthenticationException When the user could not be authenticated.
     */
    public User authenticate(String email, String password) throws AuthenticationException
    {
        User user = this.repository.withEmail(email);
        if (user == null)
            throw new IncorrectCredentialsException(new IncorrectCredentialsException());

        if (!checkHash(password, user.getPasswordHash()))
            throw new IncorrectCredentialsException(new IncorrectCredentialsException());

        return user;
    }

    /**
     * Checks that the provided {@code plainPassword} matches the provided {@code hashedPassword} using the b-crypt
     * algorithm.
     *
     * @param plainPassword  The plaintext password.
     * @param hashedPassword The hashed password.
     * @return {@code true} when the provided {@code plainPassword} matches the provided {@code hashedPassword}.
     */
    public boolean checkHash(String plainPassword, String hashedPassword)
    {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
