package com.group3.sem3exam.logic.authentication.jwt;

/**
 * The secret used when generating and verifying JWT tokens.
 */
public interface JwtSecret
{

    /**
     * Returns the secret string value.
     *
     * @return The secret byte value.
     */
    byte[] getValue();

    /**
     * Regenerates the Jwt secret.
     *
     * @param bytes The number of bytes the secret string should be generated from.
     * @return The newly generated value.
     * @throws JwtSecretGenerationException When a secret cannot be generated.
     */
    byte[] regenerate(int bytes) throws JwtSecretGenerationException;
}
