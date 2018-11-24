package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.jwt.JpaJwtSecret;
import com.group3.sem3exam.rest.dto.AuthenticationDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("authentication")
public class AuthenticationResource
{

    private static Gson                 gson = SpecializedGson.create();
    private static AuthenticationFacade authenticationFacade;

    static {
        try {
            authenticationFacade = new AuthenticationFacade(
                    new JpaJwtSecret(JpaConnection.create().createEntityManager(), 512 / 8),
                    () -> new JpaUserRepository(JpaConnection.create())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(String content) throws Exception
    {
        ReceivedAuthenticateUser receivedUser          = gson.fromJson(content, ReceivedAuthenticateUser.class);
        AuthenticationContext    authenticationContext = authenticationFacade.authenticate(receivedUser.email, receivedUser.password);
        String                   token                 = authenticationFacade.generateAuthenticationToken(authenticationContext);
        return Response.ok(gson.toJson(AuthenticationDTO.basic(token, authenticationContext.getUser()))).build();
    }

    private class ReceivedAuthenticateUser
    {
        public String email;
        public String password;
    }
}
