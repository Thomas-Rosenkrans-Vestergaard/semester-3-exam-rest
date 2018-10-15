package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.rest.authentication.jwt.BasicJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtSecret;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class BasicJwtSecretTest
{

    @Test
    public void passConstructor()
    {
        String    value    = "secret";
        JwtSecret instance = new BasicJwtSecret(value);
        assertSame(value, instance.getValue());
    }

    @Test
    public void generateConstructor()
    {
        JwtSecret instance = new BasicJwtSecret(6);
        assertEquals(8, instance.getValue().length());
    }

    @Test
    public void generateConstructorPadding()
    {
        JwtSecret instance = new BasicJwtSecret(5);
        assertEquals(8, instance.getValue().length());
    }

    @Test
    public void getValue()
    {
        String    value    = "secret";
        JwtSecret instance = new BasicJwtSecret(value);
        assertSame(value, instance.getValue());
    }

    @Test
    public void regenerate() throws Exception
    {
        JwtSecret instance = new BasicJwtSecret(null);
        assertEquals(instance.regenerate(6), instance.getValue());
        assertEquals(8, instance.getValue().length());
    }

    @Test
    public void regeneratePadding() throws Exception
    {
        JwtSecret instance = new BasicJwtSecret(null);
        assertEquals(instance.regenerate(5), instance.getValue());
        assertEquals(8, instance.getValue().length());
    }
}