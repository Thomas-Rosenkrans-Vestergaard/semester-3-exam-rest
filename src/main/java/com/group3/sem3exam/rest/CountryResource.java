package com.group3.sem3exam.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.facades.CityFacade;
import com.group3.sem3exam.facades.CountryFacade;
import com.group3.sem3exam.rest.dto.CountryDTO;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;
import com.group3.sem3exam.rest.exceptions.CountryNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;

@Path("/countries")
public class CountryResource
{

    private static Gson          gson          = new GsonBuilder().setPrettyPrinting().create();
    private static CountryFacade countryFacade = new CountryFacade(JpaConnection.emf);


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getCountry(@PathParam("id") int id) throws CountryNotFoundException
    {
        Country country = countryFacade.get(id);
        String jsonDTO = gson.toJson(new CountryDTO(country));
        return Response.ok(jsonDTO).build();


}
}
