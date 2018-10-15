package com.group3.sem3exam.rest;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaConnection
{
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("rest-api-pu");

    public static void main(String[] args)
    {
        emf.close();
    }
}
