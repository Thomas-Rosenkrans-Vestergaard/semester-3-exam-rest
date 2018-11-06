package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Supplier;

public class UserAuthenticator
{

    /**
     * The factory that produces user repositories used by this object.
     */
    private final Supplier<UserRepository> repositoryFactory;

    /**
     * Creates a new {@link UserAuthenticator}.
     *
     * @param repositoryFactory The factory that produces user repositories used by this object.
     */
    public UserAuthenticator(Supplier<UserRepository> repositoryFactory)
    {
        this.repositoryFactory = repositoryFactory;
    }

    /**
     * Attempts to authenticate using the provided user credentials.
     *
     * @param email    The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The user who was authenticated, {@code null} when the user could not be authenticated.
     * @throws AuthenticationException When the user could not be authenticated.
     */
    public AuthenticationContext authenticate(String email, String password) throws AuthenticationException
    {
        try (UserRepository repository = repositoryFactory.get()) {
            User    user      = repository.getByEmail(email);
            boolean checkHash = checkHash(password, user == null ? "" : user.getPasswordHash());

            if (user != null && checkHash)
                return AuthenticationContext.user(user);

            throw new AuthenticationException(new IncorrectCredentialsException());
        }
    }

    /**
     * Checks that the provided {@code plainPassword} matches the provided {@code hashedPassword} using the b-crypt
     * algorithm.
     *
     * @param plainPassword  The plaintext password.
     * @param hashedPassword The hashed password.
     * @return {@code true} when the provided {@code plainPassword} matches the provided {@code hashedPassword}.
     */
    private boolean checkHash(String plainPassword, String hashedPassword)
    {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
