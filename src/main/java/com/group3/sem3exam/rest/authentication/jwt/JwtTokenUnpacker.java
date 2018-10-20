package com.group3.sem3exam.rest.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.rest.authentication.AuthenticationContext;
import com.group3.sem3exam.rest.authentication.AuthenticationType;

/**
 * Verifies and unpacks the payload of a JWT authentication token.
 */
public class JwtTokenUnpacker
{

    private static final String ISS = "Social";

    /**
     * The user repository containing the users in the system.
     */
    private final UserRepository userRepository;

    /**
     * The secret to use when verifying and unpacking JWT authentication tokens.
     */
    private final JwtSecret secret;

    /**
     * Creates a new {@link JwtTokenUnpacker}.
     *
     * @param userRepository The user repository containing the users in the system.
     * @param secret         The secret to use when verifying and unpacking JWT authentication tokens.
     */
    public JwtTokenUnpacker(UserRepository userRepository, JwtSecret secret)
    {
        this.userRepository = userRepository;
        this.secret = secret;
    }

    /**
     * Verifies and unpacks the payload of the provided JWT token using the configured secret.
     *
     * @param token The token to authenticate and unpack.
     * @return The contents of the token.
     * @throws JWTVerificationException When the provided token could not be verified.
     * @throws JwtUnpackingException    When the provided token does not contain all necessary information.
     */
    public AuthenticationContext unpack(String token) throws JWTVerificationException, JwtUnpackingException
    {
        Algorithm algorithm = Algorithm.HMAC256(secret.getValue());
        DecodedJWT jwt = JWT.require(algorithm)
                            .withIssuer(ISS)
                            .build()
                            .verify(token);

        try {
            AuthenticationType type = AuthenticationType.valueOf(jwt.getClaim("type").asString());
            if (type == AuthenticationType.USER) {
                return AuthenticationContext.user(userRepository.get(jwt.getClaim("user").asInt()));
            }
        } catch (Exception e) {
            throw new JwtUnpackingException("Could not unpack authentication token.", e);
        }

        throw new JwtUnpackingException("Unknown authentication type.");
    }
}
