package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.logic.authentication.jwt.JwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtSecretGenerationException;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.persistence.*;
import java.security.SecureRandom;

public class JpaJwtSecret implements JwtSecret
{

    private final  EntityManager entityManager;
    private        byte[]        secret;
    private static SecureRandom  secureRandom = new SecureRandom();

    public JpaJwtSecret(EntityManager entityManager, int bytes) throws JwtSecretGenerationException
    {
        try {
            this.entityManager = entityManager;

            JwtEntity jwt = fetch();
            if (jwt != null)
                secret = Base64.decode(jwt.getSecret());
            else
                secret = regenerate(bytes);
        } catch (Base64DecodingException e) {
            throw new JwtSecretGenerationException(e);
        }
    }

    @Override
    public byte[] getValue()
    {
        return secret;
    }

    @Override
    public byte[] regenerate(int bytes) throws JwtSecretGenerationException
    {
        try {
            byte[] random = new byte[bytes];
            secureRandom.nextBytes(random);
            this.secret = random;
            JwtEntity jwt = store(secret);
            this.secret = Base64.decode(jwt.getSecret());
            return this.secret;
        } catch (Exception e) {
            throw new JwtSecretGenerationException(e);
        }
    }

    /**
     * Stores the provided bytes in the database.
     *
     * @param bytes The bytes to store in the database.
     * @return The newly created Jwt.
     */
    private JwtEntity store(byte[] bytes)
    {
        String    encoded = Base64.encode(bytes);
        JwtEntity jwt     = new JwtEntity(encoded);
        entityManager.getTransaction().begin();
        entityManager.persist(jwt);
        entityManager.getTransaction().commit();
        return jwt;
    }

    /**
     * Fetches the latest stored Jwt.
     *
     * @return The latest stored Jwt. {@code null} when there are no stored Jwt records.
     */
    private JwtEntity fetch()
    {
        try {
            return entityManager.createQuery("SELECT jwt FROM JwtEntity jwt ORDER BY jwt.id DESC", JwtEntity.class)
                                .setMaxResults(1)
                                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}