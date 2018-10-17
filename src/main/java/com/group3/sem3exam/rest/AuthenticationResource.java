package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.facades.AuthenticationFacade;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("authentication")
public class AuthenticationResource
{

    private static Gson                 gson                 = SpecializedGson.create();
    private static AuthenticationFacade authenticationFacade = new AuthenticationFacade(JpaConnection.emf);

    @POST
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(String content) throws Exception
    {
        ReceivedAuthenticateUser receivedUser = gson.fromJson(content, ReceivedAuthenticateUser.class);
        User                     user         = authenticationFacade.authenticate(receivedUser.email, receivedUser.password);
        String                   token        = authenticationFacade.generateAuthenticationToken(user);
        JsonObject               result       = new JsonObject();
        result.addProperty("token", token);
        result.addProperty("owner", true);
        return Response.ok(result.toString()).build();
    }

    private class ReceivedAuthenticateUser
    {
        public String email;
        public String password;
    }
}
