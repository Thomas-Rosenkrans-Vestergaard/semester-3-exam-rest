package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.facades.CityFacade;
import com.group3.sem3exam.facades.RegionFacade;
import com.group3.sem3exam.rest.dto.RegionDTO;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;
import com.group3.sem3exam.rest.exceptions.CountryNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/regions")
public class RegionRessource
{


    private static Gson         gson         = new GsonBuilder().setPrettyPrinting().create();
    //skal ikke være en cityfacade men en region
    private static RegionFacade regionFacade = new RegionFacade(JpaConnection.emf);



    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegion(@PathParam("id") int id) throws CityNotFoundException, CountryNotFoundException
    {
        Region region  =regionFacade.get(id);
        String jsonDTO = gson.toJson(RegionDTO.basic(region));
        return Response.ok(jsonDTO).build();


    }


}
