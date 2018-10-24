package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.facades.UserFacade;
import com.group3.sem3exam.rest.dto.UserDTO;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;
import com.group3.sem3exam.rest.exceptions.UserNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("users")
public class UserResource
{

    private static Gson       gson       = SpecializedGson.create();
    private static UserFacade userFacade = new UserFacade(() -> new JpaUserRepository(JpaConnection.create()));

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String content) throws CityNotFoundException
    {
        ReceivedCreateUser receivedUser = gson.fromJson(content, ReceivedCreateUser.class);
        User createdUser = userFacade.createUser(receivedUser.name,
                                                 receivedUser.email,
                                                 receivedUser.password,
                                                 receivedUser.city,
                                                 receivedUser.gender,
                                                 receivedUser.dateOfBirth);

        return Response.ok(gson.toJson(UserDTO.basic(createdUser))).build();
    }

    private class ReceivedCreateUser
    {
        public String    name;
        public String    email;
        public String    password;
        public Integer   city;
        public Gender    gender;
        public LocalDate dateOfBirth;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response getUserById(@PathParam("id") int id) throws UserNotFoundException
    {
        User   user    = userFacade.get(id);
        String jsonDTO = gson.toJson(UserDTO.basic(user));
        return Response.ok(jsonDTO).build();
    }


}
