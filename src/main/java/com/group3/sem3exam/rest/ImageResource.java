package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.SpecializedGson;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.images.ImageFacade;
import com.group3.sem3exam.logic.images.ImageThumbnailerException;
import com.group3.sem3exam.logic.images.UnsupportedImageFormatException;
import com.group3.sem3exam.rest.dto.ImageDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("images")
public class ImageResource
{

    private static Gson                        gson                 = SpecializedGson.create();
    private static ImageFacade<JpaTransaction> imageFacade          = Facades.image;
    private static AuthenticationFacade        authenticationFacade = Facades.authentication;

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
            return Response.status(CREATED).entity(gson.toJson(ImageDTO.basic(image))).build();
        }

        throw new AuthenticationException("Unsupported user type");
    }

    @GET
    @Path("user/{user: [0-9]+}")
    public Response getByUser(@PathParam("user") int user)
    throws ResourceNotFoundException
    {
        List<Image> images = imageFacade.getByUser(user);
        String      json   = gson.toJson(ImageDTO.list(images, ImageDTO::basic));
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
        paginatedView.results = ImageDTO.list(images, ImageDTO::basic);

        return Response.ok(gson.toJson(paginatedView)).build();
    }

    private class ReceivedCreateImage
    {
        public String description;
        public String data;
    }
}
