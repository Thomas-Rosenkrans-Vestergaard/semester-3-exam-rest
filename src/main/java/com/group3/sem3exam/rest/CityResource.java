package com.group3.sem3exam.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.facades.CityFacade;
import com.group3.sem3exam.rest.dto.CityDTO;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cities")
public class CityResource
{
    private static Gson       gson       = new GsonBuilder().setPrettyPrinting().create();
    private static CityFacade cityFacade = new CityFacade(JpaConnection.emf);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response getCity(@PathParam("id") int id) throws CityNotFoundException
    {
        City   city    = cityFacade.get(id);
        String jsonDTO = gson.toJson(CityDTO.basic(city));
        return Response.ok(jsonDTO).build();
    }
}
