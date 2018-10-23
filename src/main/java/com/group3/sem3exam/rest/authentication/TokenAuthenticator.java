package com.group3.sem3exam.rest.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.rest.authentication.jwt.FileJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenUnpacker;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class TokenAuthenticator
{

    /**
     * The factory that created user repositories used during authentication.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * Creates a new {@link TokenAuthenticator}.
     *
     * @param userRepositoryFactory The factory that created user repositories used during authentication.
     */
    public TokenAuthenticator(Supplier<UserRepository> userRepositoryFactory)
    {
        this.userRepositoryFactory = userRepositoryFactory;
    }

    /**
     * Authenticated the incoming token, returning an authentication context with information about the authenticated
     * user.
     *
     * @param token The token to authenticate.
     * @return The authentication context containing information about the authenticated user.
     * @throws AuthenticationException When the token cannot be authenticated.
     */
    public AuthenticationContext authenticate(String token) throws AuthenticationException
    {
        try (UserRepository userRepository = userRepositoryFactory.get()) {
            JwtTokenUnpacker   unpacker = new JwtTokenUnpacker(new FileJwtSecret(new File("jwt.secret")));
            DecodedJWT         jwt      = unpacker.unpack(token);
            AuthenticationType type     = AuthenticationType.valueOf(jwt.getClaim("type").asString());

            if (type == AuthenticationType.USER)
                return AuthenticationContext.user(userRepository.get(jwt.getClaim("user").asInt()));

            throw new RuntimeException("Unsupported authentication type.");

        } catch (IOException e) {
            throw new AuthenticationException(e);
        }
    }
}
