package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaPostRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.PostFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.rest.dto.PostDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Path("Posts")
public class PostRessource
{

    private static Gson                       gson       = SpecializedGson.create();
    private static PostFacade<JpaTransaction> postFacade = new PostFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaPostRepository(transaction)
    );

    /*
    private static TokenAuthenticator tokenAuthenticator = new TokenAuthenticator();

    @Path("user")
    public Response getPostsByUser(@HeaderParam("token")String token) throws AuthenticationException
    {
    AuthenticationContext authenticationContext =tokenAuthenticator.authenticate(token);
    User user = authenticationContext.getUser();
    postFacade.
    }
    */


    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPost(String content) throws ResourceNotFoundException
    {
        ReceivedCreatePost post        = gson.fromJson(content, ReceivedCreatePost.class);
        Post               createdPost = postFacade.createPost(post.title,
                                                               post.contents,
                                                               post.user,
                                                               post.timeCreated);
        return Response.ok(gson.toJson(PostDTO.basic(createdPost))).build();
    }


    @Path("{id: 0-9+}")
    public Response getPostById(@PathParam("id") Integer id) throws ResourceNotFoundException
    {
        Post    post    = postFacade.get(id);
        PostDTO postDTO = new PostDTO(post);
        return Response.ok(postDTO).build();
    }


    private class ReceivedCreatePost
    {
        private String        contents;
        private String        title;
        private LocalDateTime timeCreated;
        private User          user;
    }

}
