package com.group3.sem3exam.rest.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.group3.sem3exam.data.entities.User;

import java.util.Date;

/**
 * Generates JWT authentication tokens for users.
 */
public class JwtTokenGenerator
{

    /**
     * The secret to use when generating JWT authentication tokens.
     */
    private final JwtSecret secret;

    /**
     * Creates a new {@link JwtTokenGenerator}.
     *
     * @param secret The secret to use when generating JWT authentication tokens.
     */
    public JwtTokenGenerator(JwtSecret secret)
    {
        this.secret = secret;
    }

    /**
     * Generates a new JWT token for the provided user using the configured secret.
     *
     * @param user The user to generate the JWT authentication token for.
     * @return The JWT token.
     * @throws AuthenticationException
     */
    public String generate(User user) throws AuthenticationException
    {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getValue());
            return JWT.create()
                      .withIssuer("group3")
                      .withIssuedAt(new Date())
                      .withClaim("user", user.getId())
                      .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new AuthenticationException("JWTCreationError", "Could not create jwt token.", 500, exception);
        }
    }
}
