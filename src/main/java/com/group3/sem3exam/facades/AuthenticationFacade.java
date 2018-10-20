package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.repositories.TransactionalUserRepository;
import com.group3.sem3exam.rest.authentication.AuthenticationContext;
import com.group3.sem3exam.rest.authentication.AuthenticationException;
import com.group3.sem3exam.rest.authentication.UserAuthenticator;
import com.group3.sem3exam.rest.authentication.jwt.FileJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenGenerator;

import javax.persistence.EntityManagerFactory;
import java.io.File;

public class AuthenticationFacade
{

    /**
     * The entity manager factory that is used when authenticating users.
     */
    private final EntityManagerFactory emf;

    /**
     * Creates a new {@link AuthenticationFacade}.
     *
     * @param emf The entity manager factory that is used when authenticating users.
     */
    public AuthenticationFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
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
        File              file      = new File("jwt.secret");
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
     * @throws AuthenticationException When the credentials could not be used to authenticate the credentials.
     */
    public AuthenticationContext authenticate(String email, String password) throws Exception
    {
        try (TransactionalUserRepository tup = new TransactionalUserRepository(emf)) {
            UserAuthenticator authenticator = new UserAuthenticator(tup);
            return authenticator.authenticate(email, password);
        }
    }
}
