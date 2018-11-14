package com.group3.sem3exam.logic.authentication.jwt;

import java.security.SecureRandom;

public class BasicJwtSecret implements JwtSecret
{

    /**
     * The secret string value.
     */
    private byte[] secret;

    /**
     * The source of randomness the secret is generated from.
     */
    private static SecureRandom random = new SecureRandom();

    /**
     * Creates a new {@link BasicJwtSecret}.
     *
     * @param secret The secret string value.
     */
    public BasicJwtSecret(byte[] secret)
    {
        this.secret = secret;
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
     * Returns the secret byte value.
     *
     * @return The secret byte value.
     */
    @Override
    public byte[] getValue()
    {
        return secret;
    }

    /**
     * Regenerates the Jwt secret.
     *
     * @param bytes The number of bytes the secret string should be generated from.
     * @return The newly generated value.
     */
    @Override
    public byte[] regenerate(int bytes)
    {
        byte[] byteArray = new byte[bytes];
        random.nextBytes(byteArray);
        return this.secret = byteArray;
    }
}
