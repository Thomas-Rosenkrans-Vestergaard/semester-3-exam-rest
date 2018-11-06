package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.UserAuthenticator;
import com.group3.sem3exam.logic.authentication.jwt.FileJwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtTokenGenerator;

import java.io.File;
import java.util.function.Supplier;

public class AuthenticationFacade
{

    /**
     * The factory that produces user repositories used by this facade.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * Creates a new {@link AuthenticationFacade}.
     *
     * @param userRepositoryFactory The factory that produces user repositories used by this facade.
     */
    public AuthenticationFacade(Supplier<UserRepository> userRepositoryFactory)
    {
        this.userRepositoryFactory = userRepositoryFactory;
    }

    /**
     * Generates a JWT authentication token for the provided authentication context.
     *
     * @param authenticationContext The authentication context to generate the JWT token from.
     * @return The resulting JWT token.
     * @throws Exception When
     */
    public String generateAuthenticationToken(AuthenticationContext authenticationContext) throws Exception
    {
        File              file      = new File("../temp/jwt.secret");
        FileJwtSecret     jwtSecret = new FileJwtSecret(file, 256 / 8);
        JwtTokenGenerator generator = new JwtTokenGenerator(jwtSecret);
        return generator.generate(authenticationContext);
    }

    /**
     * Attempts to authenticate a user with the provided credentials.
     *
     * @param email    The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The authentication context.
     * @throws AuthenticationException When the credentials could not be used to authenticate the user.
     */
    public AuthenticationContext authenticate(String email, String password) throws AuthenticationException
    {
        UserAuthenticator authenticator = new UserAuthenticator(userRepositoryFactory);
        return authenticator.authenticate(email, password);
    }
}
