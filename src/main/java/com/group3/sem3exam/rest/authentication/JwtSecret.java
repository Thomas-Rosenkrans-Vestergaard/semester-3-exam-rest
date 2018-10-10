package com.group3.sem3exam.rest.authentication;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * The secret used when generating and verifying JWT tokens.
 */
public class JwtSecret
{

    /**
     * The secret string value.
     */
    private final String value;

    /**
     * Creates a new {@link JwtSecret}.
     *
     * @param value The secret string value.
     */
    public JwtSecret(String value)
    {
        this.value = value;
    }

    /**
     * Returns the secret string value.
     *
     * @return The secret string value.
     */
    public String getValue()
    {
        return this.value;
    }

    private static SecureRandom random = new SecureRandom();

    /**
     * Creates a new random secret with the provided number of random bytes.
     *
     * @param bytes The number of random bytes to use.
     * @return A base64 encoded string of the random bytes.
     */
    public static JwtSecret bytes(int bytes)
    {
        byte[] byteArray = new byte[bytes];
        random.nextBytes(byteArray);
        return new JwtSecret(Base64.getEncoder().encodeToString(byteArray));
    }
}
