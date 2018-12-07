package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.services.ServiceRepository;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.TokenAuthenticator;
import com.group3.sem3exam.logic.authentication.UserAuthenticator;
import com.group3.sem3exam.logic.authentication.jwt.JwtGenerationException;
import com.group3.sem3exam.logic.authentication.jwt.JwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtTokenGenerator;

import java.util.function.Supplier;

public class AuthenticationFacade
{

    /**
     * The jwt secret used to generate jwt tokens.
     */
    private final JwtSecret jwtSecret;

    /**
     * The factory that produces user repositories used by this facade.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * The factory that produces service repositories used by this facade.
     */
    private final Supplier<ServiceRepository> serviceRepositoryFactory;

    /**
     * Creates a new {@link AuthenticationFacade}.
     *
     * @param jwtSecret                The jwt secret used to generate jwt tokens.
     * @param userRepositoryFactory    The factory that produces user repositories used by this facade.
     * @param serviceRepositoryFactory The factory that produces service repositories used by this facade.
     */
    public AuthenticationFacade(
            JwtSecret jwtSecret,
            Supplier<UserRepository> userRepositoryFactory,
            Supplier<ServiceRepository> serviceRepositoryFactory)
    {
        this.jwtSecret = jwtSecret;
        this.userRepositoryFactory = userRepositoryFactory;
        this.serviceRepositoryFactory = serviceRepositoryFactory;
    }

    /**
     * Generates a JWT authentication token for the provided authentication context.
     *
     * @param authenticationContext The authentication context to generate the JWT token from.
     * @return The resulting JWT token.
     * @throws JwtGenerationException When the jwt token cannot be generated.
     */
    public String generateAuthenticationToken(AuthenticationContext authenticationContext) throws JwtGenerationException
    {
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

    /**
     * Performs authentication based on a provided JWT token.
     *
     * @param token The token to perform authentication upon
     * @return The resulting authentication context.
     * @throws AuthenticationException When the provided token cannot be authenticated.
     */
    public AuthenticationContext authenticateToken(String token) throws AuthenticationException
    {
        return new TokenAuthenticator(
                jwtSecret,
                userRepositoryFactory,
                serviceRepositoryFactory)
                .authenticate(token);
    }

    /**
     * Authenticates based on a provided HTTP Authorization Bearer header.
     *
     * @param header The provided HTTP Authorization Bearer header,
     * @return The resulting AuthenticationContext.
     * @throws AuthenticationException When the HTTP Authorization Bearer cannot be authenticated.
     */
    public AuthenticationContext authenticateBearerHeader(String header) throws AuthenticationException
    {
        if (header == null)
            throw new AuthenticationException("Requires Authorization: Bearer <token>");

        header = header.trim();
        if (header.startsWith("Bearer ")) {
            header = header.substring(7);
            return this.authenticateToken(header);
        }

        throw new AuthenticationException("Unsupported HTTP Authorization type.");
    }
}
