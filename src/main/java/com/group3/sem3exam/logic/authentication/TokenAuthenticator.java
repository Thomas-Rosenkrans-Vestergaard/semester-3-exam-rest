package com.group3.sem3exam.logic.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.logic.authentication.jwt.JwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtTokenUnpacker;

import java.util.function.Supplier;

public class TokenAuthenticator
{

    /**
     * The jwt secret used when authenticating users.
     */
    private final JwtSecret jwtSecret;

    /**
     * The factory that created user repositories used during authentication.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * Creates a new {@link TokenAuthenticator}.
     *
     * @param jwtSecret             The jwt secret used when authenticating users.
     * @param userRepositoryFactory The factory that created user repositories used during authentication.
     */
    public TokenAuthenticator(JwtSecret jwtSecret, Supplier<UserRepository> userRepositoryFactory)
    {
        this.jwtSecret = jwtSecret;
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
        JwtTokenUnpacker   unpacker = new JwtTokenUnpacker(jwtSecret);
        DecodedJWT         jwt      = unpacker.unpack(token);
        AuthenticationType type     = AuthenticationType.valueOf(jwt.getClaim("type").asString());

        if (type == AuthenticationType.USER) {
            Integer userId = jwt.getClaim("user").asInt();
            return LazyAuthenticationContext.user(userId, () -> {
                try (UserRepository userRepository = userRepositoryFactory.get()) {
                    return userRepository.get(userId);
                }
            });
        }

        throw new AuthenticationException("Unsupported authentication type.");
    }
}
