package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.*;
import com.group3.sem3exam.data.repositories.TransactionalCrudRepository;
import com.group3.sem3exam.data.repositories.TransactionalUserRepository;
import com.group3.sem3exam.facades.UserFacade;
import com.group3.sem3exam.rest.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("users")
public class UserResource
{

    private static Gson                        gson           = new GsonBuilder().setPrettyPrinting().create();
    private static UserFacade                  userFacade     = new UserFacade(JpaConnection.emf);
    private static TransactionalUserRepository crudRepository = new TransactionalUserRepository();


    /**
     *
     * @param content
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String content)
    {
        ReceivedCreateUser receivedUser = gson.fromJson(content, ReceivedCreateUser.class);
        User               createdUser  = userFacade.createUser(receivedUser.name, receivedUser.email, receivedUser.password, receivedUser.city, receivedUser.gender, receivedUser.dateOfBirth);
        return Response.ok(createdUser).build();
    }


    /**
     *
     * @param id
     * @return
     */
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id)
    {
        UserDTO userDTO = (UserDTO.basic(crudRepository.get(id)));
        String jsonDTO = gson.toJson(userDTO);
        return Response.ok(jsonDTO).build();

    }


    private class ReceivedCreateUser
    {
        public String    name;
        public String    email;
        public String    password;
        public City      city;
        public Gender    gender;
        public LocalDate dateOfBirth;
    }
}
