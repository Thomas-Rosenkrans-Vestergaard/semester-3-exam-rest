package com.group3.sem3exam.rest.authentication;

import org.junit.Test;

public class JwtSecretTest
{

    @Test
    public void getValue()
    {
    }

    @Test
    public void bytes()
    {
        JwtSecret secret = JwtSecret.bytes(128);
        System.out.println(secret.getValue());
    }
}