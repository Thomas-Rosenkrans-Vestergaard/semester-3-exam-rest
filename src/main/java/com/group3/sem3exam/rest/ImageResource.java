package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.facades.DataUriEncoder;
import com.group3.sem3exam.facades.ImageFacade;
import com.group3.sem3exam.rest.authentication.Authenticator;
import com.group3.sem3exam.rest.dto.CityDTO;
import com.group3.sem3exam.rest.dto.ImageDTO;
import com.group3.sem3exam.rest.exceptions.ImageNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Path("images")
public class ImageResource
{

    private static Gson           gson          = SpecializedGson.create();
    private static ImageFacade    imageFacade   = new ImageFacade(JpaConnection.create());
    private static DataUriEncoder encoder       = new DataUriEncoder();
    private static Authenticator  authenticator = new Authenticator();

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam("Authorization")String token, String content) throws IOException
    {

        ReceivedCreateImage image = gson.fromJson(content, ReceivedCreateImage.class);
        byte[]              data  = Base64.getDecoder().decode(image.data);
        String              URI   = encoder.bytesToDataURI(data);
        return Response.ok(URI).build();
    }


    @GET
    @Path("get")
    public Response get (){

    }



    @GET
    @Path("get/{user: [0-9]+}")
    public Response getByUser(@PathParam("user")int user, @HeaderParam("Authorization") String content) throws ImageNotFoundException
    {
    List<Image> images = imageFacade.getByUser(user);
    String json = gson.toJson(images.stream().map(ImageDTO::basic).collect(Collectors.toList()));
    return Response.ok(json).build();
    }


    private class ReceivedCreateImage
    {
        public String title;
        public String data;
    }

}
