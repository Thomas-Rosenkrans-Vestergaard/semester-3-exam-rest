package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCityRepository;
import com.group3.sem3exam.data.repositories.JpaFriendshipRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.FriendshipFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.UserFacade;
import com.group3.sem3exam.logic.validation.ResourceValidationException;
import com.group3.sem3exam.rest.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("users")
public class UserResource
{

    private static Gson                       gson       = SpecializedGson.create();
    private static UserFacade<JpaTransaction> userFacade = new UserFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaCityRepository(transaction)
    );

    private static FriendshipFacade<JpaTransaction> friendshipshipFacade = new FriendshipFacade<JpaTransaction>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaFriendshipRepository(transaction)
    );

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response createUser(String content) throws ResourceNotFoundException, ResourceValidationException
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
    @Produces(APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response getUserById(@PathParam("id") int id) throws ResourceNotFoundException
    {
        User   user    = userFacade.get(id);
        String jsonDTO = gson.toJson(UserDTO.basic(user));
        return Response.ok(jsonDTO).build();
    }


    @Path("{id: 0-9+}/friends")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getFriendsByOwnerId(@PathParam("id") Integer id)
    {
        List<User> friends = friendshipshipFacade.getUserFriends(id); //unused
        String     jsonDTO = gson.toJson(UserDTO.list(friends, UserDTO::basic));
        throw new UnsupportedOperationException("Not supported yet");
    }

    @GET
    @Path("genders")
    @Produces(APPLICATION_JSON)
    public Response getGenders()
    {
        JsonArray array = new JsonArray();
        for (Gender gender : Gender.values())
            array.add(gender.name());

        return Response.ok(gson.toJson(array)).build();
    }
}
