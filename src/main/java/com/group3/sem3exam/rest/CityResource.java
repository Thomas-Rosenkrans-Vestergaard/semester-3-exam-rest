package com.group3.sem3exam.rest;


import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.logic.CityFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.SpecializedGson;
import com.group3.sem3exam.rest.dto.CityDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("cities")
public class CityResource
{

    private static Gson gson = SpecializedGson.create();
    private static CityFacade cityFacade = Facades.city;


    @GET
    @Path("{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getCityById(@PathParam("id") int id) throws ResourceNotFoundException
    {
        City   city = cityFacade.get(id);
        String json = gson.toJson(CityDTO.basic(city));
        return Response.ok(json).build();
    }

    @GET
    @Path("region/{region: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getCitiesByRegion(@PathParam("region") int region)
    {
        List<City> cities = cityFacade.getByRegion(region);
        String     json   = gson.toJson(cities.stream().map(CityDTO::basic).collect(Collectors.toList()));
        return Response.ok(json).build();
    }
}
