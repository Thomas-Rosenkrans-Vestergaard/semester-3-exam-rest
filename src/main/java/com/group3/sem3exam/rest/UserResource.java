package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.facades.UserFacade;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource
{
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static EntityManagerFactory emf  = Persistence.createEntityManagerFactory("rest-api-pu");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() throws Exception
    {
        return "Hello World";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String content) {
        RecievedUser      u  = gson.fromJson(content, RecievedUser.class);
        UserFacade uf = new UserFacade(emf);
        User resultUser = uf.createUser( u.getName(), u.getEmail(), u.getPasswordHash() );
        return Response.ok( resultUser ).build();
    }

    private class RecievedUser {
        private String username;
        private String password;
        private String email;

        public RecievedUser(String username, String password, String email)
        {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        public String getName()
        {
            return this.username;
        }

        public String getPasswordHash()
        {
            return this.password;
        }

        public String getEmail()
        {
            return this.email;
        }
    }
}
