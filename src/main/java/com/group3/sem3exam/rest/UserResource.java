package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.facades.UserFacade;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource
{

    private static Gson       gson       = new GsonBuilder().setPrettyPrinting().create();
    private static UserFacade userFacade = new UserFacade(JpaConnection.emf);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String content)
    {
        ReceivedCreateUser receivedUser = gson.fromJson(content, ReceivedCreateUser.class);
        User               createdUser  = userFacade.createUser(receivedUser.name, receivedUser.email, receivedUser.password);
        return Response.ok(createdUser).build();
    }

    private class ReceivedCreateUser
    {
        public String name;
        public String email;
        public String password;
    }
}
