package com.group3.sem3exam.rest;


import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.logic.CountryFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.SpecializedGson;
import com.group3.sem3exam.rest.dto.CountryDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("countries")
public class CountryResource
{

    private static Gson          gson          = SpecializedGson.create();
    private static CountryFacade countryFacade = Facades.country;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllCountries()
    {
        List<Country> countries = countryFacade.all();
        String        json      = gson.toJson(countries.stream().map(CountryDTO::basic).collect(Collectors.toList()));
        return Response.ok(json).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response getCountryById(@PathParam("id") int id) throws ResourceNotFoundException
    {
        Country country = countryFacade.get(id);
        String  json    = gson.toJson(CountryDTO.withRegions(country));
        return Response.ok(json).build();
    }
}
