package com.group3.sem3exam.rest;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.*;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authorization.AuthorizationException;
import com.group3.sem3exam.rest.dto.CommentDTO;
import sun.font.CreatedFontTracker;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.group3.sem3exam.rest.Facades.post;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/comments")
public class CommentResource
{

    private static Gson                       gson                 = SpecializedGson.create();
    private static PostFacade<JpaTransaction> postFacade           = post;
    private static AuthenticationFacade       authenticationFacade = Facades.authentication;
    private static CommentFacade              commentFacade        = Facades.comments;

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{post: [0-9]+}")
    public Response getComments(@HeaderParam("Authorization") String auth, @PathParam("post") Integer post)
    throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        List<Comment>         comments              = commentFacade.getComments(authenticationContext, post);
        return Response.ok(gson.toJson(CommentDTO.list(comments, CommentDTO::basic))).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{commentparent: [0-9]+}/count")
    public Response getCommentCount(@HeaderParam("Authorization") String auth, @PathParam("commentparent") Integer id)
    throws ResourceNotFoundException
    {
        int        count    = commentFacade.count(id);
        JsonObject response = new JsonObject();
        response.addProperty("count", count);
        return Response.ok().entity(gson.toJson(response)).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{post: [0-9]+}/page/{pageSize: [0-9]+}/{pageNumber: [0-9]+}")
    public Response getCommentsPage(
            @HeaderParam("Authorization") String auth,
            @PathParam("post") Integer post,
            @PathParam("pageSize") Integer pageSize,
            @PathParam("pageNumber") Integer pageNumber)
    throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        List<Comment>         comments              = commentFacade.getCommentsPage(authenticationContext, post, pageSize, pageNumber);
        return Response.ok(gson.toJson(CommentDTO.list(comments, CommentDTO::basic))).build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("{post: [0-9]+}")
    public Response createComment(@HeaderParam("Authorization") String auth, @PathParam("post") Integer post, String json)
    throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext           authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        CommentResource.ReceivedComment receivedComment       = gson.fromJson(json, CommentResource.ReceivedComment.class);
        Comment                         comment               = commentFacade.create(authenticationContext, receivedComment.contents, post);
        return Response.status(CREATED).entity(gson.toJson(CommentDTO.basic(comment))).build();
    }


    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("emoji/{id: [0-9]+}")
    public Response addEmoji(@HeaderParam("Authorization") String auth, @PathParam("id") Integer id, String json) throws AuthenticationException, ResourceNotFoundException, AuthorizationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        CommentResource.Emoji emoji = gson.fromJson(json, CommentResource.Emoji.class);
        Comment comment =       commentFacade.addEmoji(emoji.name, emoji.count, id, authenticationContext);
        return Response.status(CREATED).entity(gson.toJson(CommentDTO.withEmoji(comment))).build();

    }

    @DELETE
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response deleteComment(@HeaderParam("Authorization") String auth, @PathParam("id") Integer id)
    throws AuthenticationException, ResourceNotFoundException, AuthorizationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        Comment               comment               = commentFacade.delete(authenticationContext, id);
        return Response.ok(gson.toJson(CommentDTO.basic(comment))).build();
    }

    private class Emoji{
        private String name;
        private int count;
    }

    private class ReceivedComment
    {
        public String contents;
    }
}
