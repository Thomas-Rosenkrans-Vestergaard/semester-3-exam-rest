package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.facades.UserFacade;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource
{
    private        Gson                 gson       = new GsonBuilder().setPrettyPrinting().create();
    private static EntityManagerFactory emf        = Persistence.createEntityManagerFactory("rest-api-pu");
    private static UserFacade           userFacade = new UserFacade(emf);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() throws Exception
    {
        return "Hello World";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String content)
    {
        ReceivedUser receivedUser = gson.fromJson(content, ReceivedUser.class);
        User         createdUser  = userFacade.createUser(receivedUser.name, receivedUser.email, receivedUser.password);
        return Response.ok(createdUser).build();
    }

    private class ReceivedUser
    {
        public String name;
        public String email;
        public String password;
    }
}
