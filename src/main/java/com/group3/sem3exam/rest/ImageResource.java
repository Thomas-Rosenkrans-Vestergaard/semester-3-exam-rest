package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.CommentFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.SpecializedGson;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.images.ImageFacade;
import com.group3.sem3exam.logic.images.ImageThumbnailerException;
import com.group3.sem3exam.logic.images.UnsupportedImageFormatException;
import com.group3.sem3exam.rest.dto.CommentDTO;
import com.group3.sem3exam.rest.dto.ImageDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("images")
public class ImageResource
{

    private static Gson                        gson                 = SpecializedGson.create();
    private static ImageFacade<JpaTransaction> imageFacade          = Facades.image;
    private static AuthenticationFacade        authenticationFacade = Facades.authentication;
    private static CommentFacade               commentFacade        = Facades.comments;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam("Authorization") String token, String content)
    throws UnsupportedImageFormatException, AuthenticationException, ImageThumbnailerException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        if (authenticationContext.getType() == USER) {
            ReceivedCreateImage receivedCreateImage = gson.fromJson(content, ReceivedCreateImage.class);
            Image image = imageFacade.create(
                    authenticationContext.getUser(),
                    receivedCreateImage.description,
                    receivedCreateImage.data);
            return Response.status(CREATED).entity(gson.toJson(ImageDTO.complete(image))).build();
        }

        throw new AuthenticationException("Unsupported user type");
    }

    @GET
    @Path("user/{user: [0-9]+}")
    public Response getByUser(@PathParam("user") int user)
    throws ResourceNotFoundException
    {
        List<Image> images = imageFacade.getByUser(user);
        String      json   = gson.toJson(ImageDTO.list(images, ImageDTO::complete));
        return Response.ok(json).build();
    }

    private class PaginatedView
    {
        public int            count;
        public List<ImageDTO> results;
    }

    @GET
    @Path("user/{user: [0-9]+}/count")
    public Response countByUser(@PathParam("user") int user) throws ResourceNotFoundException
    {
        int        count  = imageFacade.countByUser(user);
        JsonObject result = new JsonObject();
        result.addProperty("count", count);
        return Response.ok(gson.toJson(result)).build();
    }

    @GET
    @Path("user/{user: [0-9]+}/{pageSize: [0-9]+}/{pageNumber: [0-9]+}")
    public Response getByUserPaginated(
            @PathParam("user") int user,
            @PathParam("pageSize") int pageSize,
            @PathParam("pageNumber") int pageNumber)
    throws ResourceNotFoundException
    {
        List<Image>   images        = imageFacade.getByUserPaginated(user, pageSize, pageNumber);
        PaginatedView paginatedView = new PaginatedView();
        paginatedView.count = imageFacade.countByUser(user);
        paginatedView.results = ImageDTO.list(images, ImageDTO::complete);

        return Response.ok(gson.toJson(paginatedView)).build();
    }

    private class ReceivedCreateImage
    {
        public String description;
        public String data;
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("{image: [0-9]+}/comments")
    public Response createComment(@HeaderParam("Authorization") String auth, @PathParam("image") Integer image, String json)
    throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        ReceivedComment       receivedComment       = gson.fromJson(json, ReceivedComment.class);
        Comment               comment               = commentFacade.create(authenticationContext, receivedComment.contents, image);
        return Response.status(CREATED).entity(gson.toJson(CommentDTO.basic(comment))).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{image: [0-9]+}/comments/count")
    public Response createComment(@HeaderParam("Authorization") String auth, @PathParam("image") Integer id)
    throws ResourceNotFoundException
    {
        int        count    = commentFacade.count(id);
        JsonObject response = new JsonObject();
        response.addProperty("count", count);
        return Response.ok().entity(gson.toJson(response)).build();
    }

    private class ReceivedComment
    {
        public String contents;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{image: [0-9]+}/comments")
    public Response getComments(@HeaderParam("Authorization") String auth, @PathParam("image") Integer image)
    throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        List<Comment>         comments              = commentFacade.getComments(authenticationContext, image);
        return Response.ok(gson.toJson(CommentDTO.list(comments, CommentDTO::basic))).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{image: [0-9]+}/comments/page/{pageSize: [0-9]+}/{pageNumber: [0-9]+}")
    public Response getCommentsPage(
            @HeaderParam("Authorization") String auth,
            @PathParam("image") Integer image,
            @PathParam("pageSize") Integer pageSize,
            @PathParam("pageNumber") Integer pageNumber)
    throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        List<Comment>         comments              = commentFacade.getCommentsPage(authenticationContext, image, pageSize, pageNumber);
        return Response.ok(gson.toJson(CommentDTO.list(comments, CommentDTO::basic))).build();
    }
}
