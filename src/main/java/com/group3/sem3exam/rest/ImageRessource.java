package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.facades.CountryFacade;
import com.group3.sem3exam.facades.ImageFacade;
import com.group3.sem3exam.rest.dto.ImageDTO;
import com.group3.sem3exam.rest.exceptions.ImageNotFoundException;

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

    private static Gson        gson          = SpecializedGson.create();
    private static ImageFacade imageFacade = new ImageFacade(JpaConnection.emf);

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response getImageByID(@PathParam("id") int id) throws ImageNotFoundException
    {
       Image image =  imageFacade.get(id);
       ImageDTO dto = ImageDTO.basic(image);
       byte[] encodedData = Base64.getEncoder().encode(dto.getData());


    }

}
