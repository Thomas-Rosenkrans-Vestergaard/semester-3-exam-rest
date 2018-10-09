package com.group3.sem3exam.rest;

import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserResource
{

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get()
    {
        Persistence.createEntityManagerFactory("rest-api-pu");

        return "Hello world";
    }
}
