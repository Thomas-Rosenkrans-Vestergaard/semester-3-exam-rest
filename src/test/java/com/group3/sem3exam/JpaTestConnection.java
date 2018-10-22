package com.group3.sem3exam;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaTestConnection
{
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("rest-api-test-pu");

    public static void main(String[] args)
    {
        emf.close();
    }
}
