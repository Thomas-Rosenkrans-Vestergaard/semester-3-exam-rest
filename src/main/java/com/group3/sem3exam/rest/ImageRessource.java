package com.group3.sem3exam.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.Base64;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("images")
public class ImageRessource
{
    @GET
    @Produces(APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response getImagebyID(@PathParam("id") int id)
    {
        Base64.getEncoder().encode()
    }

}
