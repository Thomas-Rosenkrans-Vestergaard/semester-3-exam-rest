package com.group3.sem3exam.logic.authentication.jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class BasicJwtSecret implements JwtSecret
{

    /**
     * The secret string value.
     */
    private String value;

    /**
     * The source of randomness the secret is generated from.
     */
    private static SecureRandom random = new SecureRandom();

    /**
     * Creates a new {@link BasicJwtSecret}.
     *
     * @param value The secret string value.
     */
    public BasicJwtSecret(String value)
    {
        this.value = value;
    }

    /**
     * Creates a new {@link BasicJwtSecret}, generating a random bas64 encoded of the provided size in bytes.
     *
     * @param bytes The number of random bytes the base64 secret should be generated from.
     */
    public BasicJwtSecret(int bytes)
    {
        regenerate(bytes);
    }

    /**
     * Returns the secret string value.
     *
     * @return The secret string value.
     */
    @Override
    public String getValue()
    {
        return value;
    }

    /**
     * Regenerates the Jwt secret.
     *
     * @param bytes The number of bytes the secret string should be generated from.
     * @return The newly generated value.
     */
    @Override
    public String regenerate(int bytes)
    {
        byte[] byteArray = new byte[bytes];
        random.nextBytes(byteArray);
        return this.value = Base64.getEncoder().encodeToString(byteArray);
    }
}
