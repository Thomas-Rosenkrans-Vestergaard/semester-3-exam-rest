package com.group3.sem3exam.logic.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.services.Service;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;

import java.util.Date;

/**
 * Generates JWT authentication tokens for users.
 */
public class JwtTokenGenerator
{

    private static final String ISS = "Social";

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
     * Generates a new JWT token for the provided authentication context, signed using the configured secret.
     *
     * @param authenticationContext The authentication context to tokenize.
     * @return The JWT token.
     * @throws JwtGenerationException When the method could not generate a Jwt token.
     */
    public String generate(AuthenticationContext authenticationContext) throws JwtGenerationException
    {
        try {
            Algorithm          algorithm = Algorithm.HMAC256(secret.getValue());
            JWTCreator.Builder builder   = JWT.create().withIssuer(ISS).withIssuedAt(new Date());
            withClaims(authenticationContext, builder);
            return builder.sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JwtGenerationException(exception);
        }
    }

    /**
     * Adds the necessary claims to the provided builder.
     *
     * @param authenticationContext The authentication context.
     * @param builder               The builder to add claims to.
     */
    private void withClaims(AuthenticationContext authenticationContext, JWTCreator.Builder builder)
    {
        builder.withClaim("type", authenticationContext.getType().name());

        User user = authenticationContext.getUser();
        if (user != null)
            builder.withClaim("user", user.getId());

        Service service = authenticationContext.getService();
        if (service != null)
            builder.withClaim("service", service.getId());
    }
}
