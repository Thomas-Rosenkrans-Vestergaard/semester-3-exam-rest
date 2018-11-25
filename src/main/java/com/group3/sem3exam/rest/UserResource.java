package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.ProfilePicture;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCityRepository;
import com.group3.sem3exam.data.repositories.JpaFriendshipRepository;
import com.group3.sem3exam.data.repositories.JpaImageRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.*;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.jwt.JpaJwtSecret;
import com.group3.sem3exam.logic.images.CropArea;
import com.group3.sem3exam.logic.images.ImageCropperException;
import com.group3.sem3exam.logic.images.ImageThumbnailerException;
import com.group3.sem3exam.logic.images.UnsupportedImageFormatException;
import com.group3.sem3exam.logic.validation.ResourceValidationException;
import com.group3.sem3exam.rest.dto.ImageDTO;
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
            transaction -> new JpaCityRepository(transaction),
            transaction -> new JpaImageRepository(transaction)
    );

    private static AuthenticationFacade authenticationFacade;

    static {
        try {
            authenticationFacade = new AuthenticationFacade(
                    new JpaJwtSecret(JpaConnection.create().createEntityManager(), 512 / 8),
                    () -> new JpaUserRepository(JpaConnection.create())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static FriendshipFacade<JpaTransaction> friendshipFacade = new FriendshipFacade<JpaTransaction>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaFriendshipRepository(transaction)
    );

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response createUser(String content) throws ResourceNotFoundException,
                                                      ResourceValidationException,
                                                      ResourceConflictException
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

    @PUT
    @Path("{user: [0-9]+}/profile-image")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updateProfileImage(
            @HeaderParam("Authorization") String token,
            @PathParam("user") Integer user,
            String json) throws AuthenticationException, ResourceNotFoundException, ImageCropperException, UnsupportedImageFormatException, ImageThumbnailerException
    {
        UpdateProfileImagePost post = gson.fromJson(json, UpdateProfileImagePost.class);
        ProfilePicture profileImage = userFacade.updateProfileImage(
                authenticationFacade.authenticateBearerHeader(token),
                user,
                post.data,
                post.crop);

        return Response.ok(gson.toJson(ImageDTO.basic(profileImage))).build();
    }

    private class UpdateProfileImagePost
    {
        public String   data;
        public CropArea crop;
    }

    @GET
    @Path("{user: [0-9]+}/friends")
    @Produces(APPLICATION_JSON)
    public Response getFriends(@PathParam("user") Integer userId) throws ResourceNotFoundException
    {
        List<User> friends = friendshipFacade.getFriends(userId);
        return Response.ok(gson.toJson(UserDTO.list(friends, UserDTO::hideSensitive))).build();
    }

    @GET
    @Path("{user: [0-9]+}/friends/{pageSize: [0-9]+}/{pageNumber: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getFriends(
            @PathParam("user") Integer userId,
            @PathParam("pageSize") Integer pageSize,
            @PathParam("pageNumber") Integer pageNumber,
            @QueryParam("search") String search)
    throws ResourceNotFoundException
    {
        List<User> friends = friendshipFacade.searchFriends(userId, pageSize, pageNumber, search);
        return Response.ok(gson.toJson(UserDTO.list(friends, UserDTO::hideSensitive))).build();
    }
}
