package com.group3.sem3exam.rest.authentication.jwt;

import com.group3.sem3exam.rest.authentication.SecretGenerationException;

/**
 * The secret used when generating and verifying JWT tokens.
 */
public interface JwtSecret
{

    /**
     * Returns the secret string value.
     *
     * @return The secret string value.
     */
    String getValue();

    /**
     * Regenerates the Jwt secret.
     *
     * @param bytes The number of bytes the secret string should be generated from.
     * @return The newly generated value.
     * @throws SecretGenerationException When a secret cannot be generated.
     */
    String regenerate(int bytes) throws SecretGenerationException;
}
