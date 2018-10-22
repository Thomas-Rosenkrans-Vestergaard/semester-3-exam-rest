package com.group3.sem3exam.rest;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaConnection
{
    private static EntityManagerFactory emf;

    public static EntityManagerFactory create()
    {
        if (emf == null)
            emf = Persistence.createEntityManagerFactory("rest-api-pu");

        return emf;
    }
}
