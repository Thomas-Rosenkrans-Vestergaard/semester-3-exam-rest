package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.data.repositories.JpaRegionRepository;
import com.group3.sem3exam.facades.RegionFacade;
import com.group3.sem3exam.facades.ResourceNotFoundException;
import com.group3.sem3exam.rest.dto.RegionDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("regions")
public class RegionResource
{

    private static Gson         gson         = SpecializedGson.create();
    private static RegionFacade regionFacade = new RegionFacade(() -> new JpaRegionRepository(JpaConnection.create()));

    @GET
    @Path("{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getRegionById(@PathParam("id") int id) throws ResourceNotFoundException
    {
        Region region = regionFacade.get(id);
        String json   = gson.toJson(RegionDTO.basic(region));
        return Response.ok(json).build();
    }

    @GET
    @Path("country/{country: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getRegionsByCountry(@PathParam("country") int country)
    {
        List<Region> regions = regionFacade.getByCountry(country);
        String       json    = gson.toJson(regions.stream().map(RegionDTO::basic).collect(Collectors.toList()));
        return Response.ok(json).build();
    }
}
