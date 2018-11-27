package com.group3.sem3exam.data.services;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;

public class SecureRandomGenerator implements IdentifierGenerator
{
    public static final String       name   = "SecureRandomGenerator";
    private static      SecureRandom random = new SecureRandom();

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object)
    throws HibernateException
    {
        byte bytes[] = new byte[64];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }
}