package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.facades.CityFacade;
import com.group3.sem3exam.facades.UserFacade;
import com.group3.sem3exam.rest.dto.CityDTO;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/regions")
public class RegionRessource
{


    private static Gson       gson       = new GsonBuilder().setPrettyPrinting().create();
    private static CityFacade cityFacade = new CityFacade(JpaConnection.emf);



    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegion(@PathParam("id") int id) throws CityNotFoundException
    {
        City city =cityFacade.get(id);
        String jsonDTO = gson.toJson(CityDTO.basic(city));
        return Response.ok(jsonDTO).build();


    }


}
