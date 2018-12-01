package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.*;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authorization.AuthorizationException;
import com.group3.sem3exam.rest.dto.DTO;
import com.group3.sem3exam.rest.dto.FriendRequestDTO;
import com.group3.sem3exam.rest.dto.FriendshipDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("friendship")
public class FriendshipResource
{
    private static Gson                             gson                 = SpecializedGson.create();
    private static FriendshipFacade<JpaTransaction> friendshipFacade     = Facades.friendship;
    private static AuthenticationFacade             authenticationFacade = Facades.authentication;

    @POST
    @Path("requests")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response friendRequest(@HeaderParam("Authorization") String token, String contents)
    throws ResourceNotFoundException, AuthenticationException, IllegalOperationException
    {
        PostedFriendship      post                  = gson.fromJson(contents, PostedFriendship.class);
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        FriendRequest         friendRequest         = friendshipFacade.createRequest(authenticationContext, post.receiver);
        FriendRequestDTO      friendRequestDTO      = FriendRequestDTO.complete(friendRequest);

        return Response.status(CREATED).entity(gson.toJson(friendRequestDTO)).build();
    }

    private class PostedFriendship
    {
        public Integer receiver;
    }

    @POST
    @Path("requests/{request: [0-9]+}/accept")
    @Produces(APPLICATION_JSON)
    public Response accept(@HeaderParam("Authorization") String token, @PathParam("request") Integer request)
    throws AuthenticationException, AuthorizationException, ResourceNotFoundException, ResourceConflictException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        Friendship            friendship            = friendshipFacade.accept(authenticationContext, request);
        return Response.status(CREATED).entity(gson.toJson(FriendshipDTO.complete(friendship))).build();
    }

    @POST
    @Path("requests/{request: [0-9]+}/reject")
    @Produces(APPLICATION_JSON)
    public Response reject(@HeaderParam("Authorization") String token, @PathParam("request") Integer request)
    throws AuthenticationException, AuthorizationException, ResourceNotFoundException, ResourceConflictException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        FriendRequest         created               = friendshipFacade.reject(authenticationContext, request);
        return Response.status(CREATED).entity(gson.toJson(FriendRequestDTO.complete(created))).build();
    }

    @GET
    @Path("receiver/{receiver: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getFriendship(@HeaderParam("Authorization") String token, @PathParam("receiver") Integer other)
    throws AuthenticationException, ResourceNotFoundException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        Friendship            friendship            = friendshipFacade.getFriendship(authenticationContext, other);
        return Response.ok(gson.toJson(FriendshipDTO.complete(friendship))).build();
    }

    @GET
    @Path("requests/receiver/{receiver: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getRequest(@HeaderParam("Authorization") String token, @PathParam("receiver") Integer other)
    throws AuthenticationException, ResourceNotFoundException, AuthorizationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        FriendRequest         request               = friendshipFacade.getRequest(authenticationContext, other);
        return Response.ok(gson.toJson(FriendRequestDTO.complete(request))).build();
    }

    @GET
    @Path("requests/received")
    @Produces(APPLICATION_JSON)
    public Response getReceivedRequests(@HeaderParam("Authorization") String token) throws AuthenticationException, AuthorizationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        List<FriendRequest>   requests              = friendshipFacade.getReceivedRequests(authenticationContext);
        return Response.ok(gson.toJson(DTO.map(requests, FriendRequestDTO::complete))).build();
    }
}
