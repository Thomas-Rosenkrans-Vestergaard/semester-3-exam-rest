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
import com.group3.sem3exam.rest.dto.FriendshipDTO;

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
    public Response friendRequest(@HeaderParam("Authorization") String token, @QueryParam("id") int userRecieverId) throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);

        if (authenticationContext.getType() == USER) {
            User             requester        = authenticationContext.getUser();
            User             reciever         = userFacade.get(userRecieverId);
            FriendRequest    friendRequest    = friendshipFacade.createFriendRequest(requester, reciever);
            FriendRequestDTO friendRequestDTO = FriendRequestDTO.basicFriendRequest(friendRequest, FriendRequest.FRIENDSHIP_STATUS.PENDING);

            return Response.status(CREATED).entity(gson.toJson(friendRequestDTO)).build();
        }

        throw new AuthenticationException("Please login fucktard");
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response friendship(@HeaderParam("Authorization") String token, @QueryParam("id") int friendRequestId) throws AuthenticationException, ResourceNotFoundException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);

        if (authenticationContext.getType() == USER) {
            Friendship    friendship    = friendshipFacade.createFriendship(friendRequestId);
            FriendshipDTO friendshipDTO = FriendshipDTO.basicFriendshipDTO(friendship);
            return Response.ok(CREATED).entity(gson.toJson(friendshipDTO)).build();
        }

        throw new AuthenticationException("unable to generate friendship, you must be logged in");
    }

    @GET
    @Produces(APPLICATION_JSON)
    public String any()
    {
        return "hi";
    }
}
