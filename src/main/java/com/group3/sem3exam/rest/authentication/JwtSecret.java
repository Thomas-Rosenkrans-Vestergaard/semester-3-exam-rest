package com.group3.sem3exam.rest.authentication;

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
     * @return The old secret value.
     */
    String regenerate(int bytes) throws Exception;
}
