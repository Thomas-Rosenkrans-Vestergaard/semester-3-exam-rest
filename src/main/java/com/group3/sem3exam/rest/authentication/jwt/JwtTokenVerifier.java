package com.group3.sem3exam.rest.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Verifies and unpacks the payload of a JWT authentication token.
 */
public class JwtTokenVerifier
{

    /**
     * The secret to use when verifying and unpacking JWT authentication tokens.
     */
    private final JwtSecret secret;

    /**
     * Creates a new {@link JwtTokenVerifier}.
     *
     * @param secret The secret to use when verifying and unpacking JWT authentication tokens.
     */
    public JwtTokenVerifier(JwtSecret secret)
    {
        this.secret = secret;
    }

    /**
     * Verifies and unpacks the payload of the provided JWT token using the configured secret.
     *
     * @param token The token to authenticate and unpack.
     * @return The contents of the token.
     * @throws JWTVerificationException When the provided token could not be verified.
     */
    public JwtTokenContents unpack(String token) throws JWTVerificationException
    {
        Algorithm algorithm = Algorithm.HMAC256(secret.getValue());
        JWTVerifier verifier = JWT.require(algorithm)
                                  .withIssuer("group3")
                                  .build();
        DecodedJWT jwt = verifier.verify(token);
        return new JwtTokenContents(jwt.getClaim("user").asInt());
    }
}
