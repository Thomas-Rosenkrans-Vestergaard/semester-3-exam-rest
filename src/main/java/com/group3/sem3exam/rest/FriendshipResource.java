package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.*;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.rest.dto.FriendRequestDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("friendship")
public class FriendshipResource
{
    private static Gson                             gson                 = SpecializedGson.create();
    private static UserFacade<JpaTransaction>       userFacade           = Facades.user;
    private static FriendshipFacade<JpaTransaction> friendshipFacade     = Facades.friendship;
    private static AuthenticationFacade             authenticationFacade = Facades.authentication;

    @POST
    @Path("friend-request")
    @Produces(APPLICATION_JSON)
    public Response friendRequest(@HeaderParam("Authorization") String token, @QueryParam("id") int id) throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);

        if (authenticationContext.getType() == USER) {
            //RecievedClass    recievedClass    = gson.fromJson(content, RecievedClass.class);
            User             requester        = authenticationContext.getUser();
            User             reciever         = userFacade.get(id);
            FriendRequest    friendRequest    = friendshipFacade.createFriendRequest(requester, reciever);
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
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);

        if (authenticationContext.getType() == USER) {
            RecievedClass recievedClass = gson.fromJson(content, RecievedClass.class);
            Friendship    friendship    = friendshipFacade.createFriendship(recievedClass.id);
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
