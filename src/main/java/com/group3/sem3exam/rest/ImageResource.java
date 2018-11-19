package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.repositories.JpaImageRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.logic.images.ImageFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.images.DataUriEncoder;
import com.group3.sem3exam.logic.images.UnsupportedImageTypeException;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.TokenAuthenticator;
import com.group3.sem3exam.rest.dto.ImageDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;

@Path("images")
public class ImageResource
{

    private static Gson           gson        = SpecializedGson.create();
    private static ImageFacade    imageFacade = new ImageFacade(() -> new JpaImageRepository(JpaConnection.create()));
    private static DataUriEncoder encoder     = new DataUriEncoder();


    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam("Authorization") String token, String content)
    throws AuthenticationException, UnsupportedImageTypeException
    {
        TokenAuthenticator    authenticator         = new TokenAuthenticator(() -> new JpaUserRepository(JpaConnection.create()));
        AuthenticationContext authenticationContext = authenticator.authenticateBearerHeader(token);
        if (authenticationContext.getType() == USER) {
            ReceivedCreateImage receivedCreateImage = gson.fromJson(content, ReceivedCreateImage.class);
            Image image = imageFacade.create(
                    authenticationContext.getUser(),
                    receivedCreateImage.title,
                    receivedCreateImage.data);
            return Response.ok(ImageDTO.basic(image)).build();
        }

        throw new AuthenticationException("Unsupported usertype");
    }

    @GET
    @Path("get/{user: [0-9]+}")
    public Response getByUser(@PathParam("user") int user, @HeaderParam("Authorization") String content)
    throws ResourceNotFoundException
    {
        List<Image> images = imageFacade.getByUser(user);
        String      json   = gson.toJson(images.stream().map(ImageDTO::basic).collect(Collectors.toList()));
        return Response.ok(json).build();
    }

    private class ReceivedCreateImage
    {
        public String title;
        public String data;
    }
}
