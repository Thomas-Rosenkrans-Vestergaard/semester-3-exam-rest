package com.group3.sem3exam.logic.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.logic.authentication.jwt.FileJwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtTokenUnpacker;

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

            if (type == AuthenticationType.USER) {
                User user = userRepository.get(jwt.getClaim("user").asInt());
                if (user == null)
                    throw new AuthenticationException("Unknown user in authorization token.");

                return AuthenticationContext.user(userRepository.get(jwt.getClaim("user").asInt()));
            }

            throw new AuthenticationException("Unsupported authentication type.");

        } catch (IOException e) {
            throw new AuthenticationException(e);
        }
    }


    /**
     *
     * @param header
     * @return the given AuthenticationContext
     * @throws AuthenticationException
     */

    public AuthenticationContext authenticateBearerHeader(String header) throws AuthenticationException
    {
            header = header.trim();
        if(header.startsWith("Bearer ")) {
            header =header.substring(7);
          return this.authenticate(header);

        }
        throw new AuthenticationException("Unsupported HTTP Authorization type.");
    }
}
