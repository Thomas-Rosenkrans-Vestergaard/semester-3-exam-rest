package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCityRepository;
import com.group3.sem3exam.data.repositories.JpaFriendshipRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.FriendshipFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.UserFacade;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.TokenAuthenticator;
import com.group3.sem3exam.rest.dto.FriendRequestDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("friendship")
public class FriendshipResource
{
    private static Gson                       gson       = SpecializedGson.create();
    private static UserFacade<JpaTransaction> userFacade = new UserFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaCityRepository(transaction)
    );
    private static FriendshipFacade<JpaTransaction> friendshidshipFacade = new FriendshipFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaFriendshipRepository(transaction)
    );

    //uses auth
    @POST
    @Path("friend-request")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response friendRequest(@HeaderParam("Authorization") String token, String content) throws ResourceNotFoundException, AuthenticationException
    {
        TokenAuthenticator    authenticator         = new TokenAuthenticator(() -> new JpaUserRepository(JpaConnection.create()));
        AuthenticationContext authenticationContext = authenticator.authenticateBearerHeader(token);

        if (authenticationContext.getType() == USER) {
            RecievedClass    recievedClass  = gson.fromJson(content, RecievedClass.class);
            User             requester        = authenticationContext.getUser();
            User             reciever         = userFacade.get(recievedClass.id);
            FriendRequest    friendRequest    = new FriendRequest(requester, reciever);
            FriendRequestDTO friendRequestDTO = FriendRequestDTO.basicFriendRequest(friendRequest, FriendRequest.FRIENDSHIP_STATUS.PENDING);

            return Response.status(CREATED).entity(friendRequestDTO).build();
        }

        throw new AuthenticationException("Please login fucktard");
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response friendship(@HeaderParam("Authorization") String token, String content) throws AuthenticationException, ResourceNotFoundException
    {
        TokenAuthenticator    authenticator         = new TokenAuthenticator(() -> new JpaUserRepository(JpaConnection.create()));
        AuthenticationContext authenticationContext = authenticator.authenticateBearerHeader(token);

        if (authenticationContext.getType() == USER) {
            RecievedClass recievedClass = gson.fromJson(content, RecievedClass.class);
            Friendship    friendship   = friendshidshipFacade.createFriendship( recievedClass.id );
            return Response.ok(CREATED).entity(friendship).build();
        }

        throw new AuthenticationException("unable to generate friendship, you must be logged in");
    }


    private class RecievedClass
    {
        public Integer id;
        /* alternative if you don't want a class
        JsonElement json = gson.toJsonTree(content);
        int id = json.getAsJsonObject().get("reciever").getAsJsonPrimitive().getAsInt();
        */
    }
/*
Friendship fs = friendshipshipFacade.createFriendship(friendRequest);
        return Response.ok(gson.toJson(fs)).build();
 */
}
