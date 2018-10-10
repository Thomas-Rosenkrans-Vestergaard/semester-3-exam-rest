package com.group3.sem3exam.rest;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserResource
{

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("rest-api-pu");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() throws Exception
    {
        return "Hello World";
    }
}
