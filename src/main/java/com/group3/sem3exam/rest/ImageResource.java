package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.facades.DataUriEncoder;
import com.group3.sem3exam.facades.ImageFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Base64;

@Path("images")
public class ImageResource
{

    private static Gson           gson        = SpecializedGson.create();
    private static ImageFacade    imageFacade = new ImageFacade(JpaConnection.create());
    private static DataUriEncoder encoder     = new DataUriEncoder();

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam("Authorization") String content) throws IOException
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

    private class ReceivedCreateImage
    {
        public String title;
        public String data;
    }

}
