package com.group3.sem3exam.logic.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.services.entities.Permission;
import com.group3.sem3exam.data.services.entities.Service;
import com.group3.sem3exam.data.services.ServiceRepository;
import com.group3.sem3exam.logic.authentication.jwt.JwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtTokenUnpacker;

import java.util.HashSet;
import java.util.function.Supplier;

public class TokenAuthenticator
{

    /**
     * The jwt secret used when authenticating users.
     */
    private final JwtSecret jwtSecret;

    /**
     * The factory that creates user repositories used during authentication.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * The factory that creates service repositories used during authentication.
     */
    private final Supplier<ServiceRepository> serviceRepositoryFactory;

    /**
     * Creates a new {@link TokenAuthenticator}.
     *
     * @param jwtSecret                The jwt secret used when authenticating users.
     * @param userRepositoryFactory    The factory that created user repositories used during authentication.
     * @param serviceRepositoryFactory The factory that creates service repositories used during authentication.
     */
    public TokenAuthenticator(
            JwtSecret jwtSecret,
            Supplier<UserRepository> userRepositoryFactory,
            Supplier<ServiceRepository> serviceRepositoryFactory)
    {
        this.jwtSecret = jwtSecret;
        this.userRepositoryFactory = userRepositoryFactory;
        this.serviceRepositoryFactory = serviceRepositoryFactory;
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
            return LazyAuthenticationContext.user(userId, createUserRetriever(userId));
        }

        if (type == AuthenticationType.SERVICE) {
            Integer serviceId = jwt.getClaim("service").asInt();
            return LazyAuthenticationContext.service(serviceId, createServiceRetriever(serviceId));
        }

        if (type == AuthenticationType.SERVICE_REPRESENTING_USER) {
            Integer userId    = jwt.getClaim("user").asInt();
            Integer serviceId = jwt.getClaim("service").asInt();
            return LazyAuthenticationContext.serviceUser(
                    serviceId, createServiceRetriever(serviceId),
                    userId, createUserRetriever(userId),
                    new HashSet<>(jwt.getClaim("permissions").asList(Permission.class))
            );
        }

        throw new AuthenticationException("Unsupported authentication type.");
    }

    private Supplier<User> createUserRetriever(Integer id)
    {
        return () -> {
            try (UserRepository userRepository = userRepositoryFactory.get()) {
                return userRepository.get(id);
            }
        };
    }

    private Supplier<Service> createServiceRetriever(Integer id)
    {
        return () -> {
            try (ServiceRepository serviceRepository = serviceRepositoryFactory.get()) {
                return serviceRepository.get(id);
            }
        };
    }
}
