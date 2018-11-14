package com.group3.sem3exam.logic.authentication.jwt;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicJwtSecretTest
{

    @Test
    public void passConstructor()
    {
        byte[]    value    = randomByteArray(5);
        JwtSecret instance = new BasicJwtSecret(value);
        assertEquals(value, instance.getValue());
    }

    @Test
    public void generateConstructor()
    {
        JwtSecret instance = new BasicJwtSecret(6);
        assertEquals(6, instance.getValue().length);
    }

    @Test
    public void getValue()
    {
        byte[]    value    = randomByteArray(5);
        JwtSecret instance = new BasicJwtSecret(value);
        assertEquals(value, instance.getValue());
    }

    @Test
    public void regenerate() throws Exception
    {
        JwtSecret instance = new BasicJwtSecret(null);
        assertEquals(instance.regenerate(6), instance.getValue());
        assertEquals(6, instance.getValue().length);
    }

    private byte[] randomByteArray(int size)
    {
        byte[]       array        = new byte[size];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(array);
        return array;
    }
}