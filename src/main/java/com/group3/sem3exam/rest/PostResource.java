package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.PostFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.SpecializedGson;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.images.ImageThumbnailerException;
import com.group3.sem3exam.logic.images.UnsupportedImageFormatException;
import com.group3.sem3exam.rest.dto.PostDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("posts")
public class PostResource
{
    private static Gson                       gson                 = SpecializedGson.create();
    private static PostFacade<JpaTransaction> postFacade           = Facades.post;
    private static AuthenticationFacade       authenticationFacade = Facades.authentication;

    @GET
    @Path("user/{userId: [0-9]+}")
    public Response getPostByUser(@PathParam("userId") Integer id) throws ResourceNotFoundException
    {
        List<Post>    posts    = postFacade.getPostByUser(id);
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            postDTOS.add(new PostDTO(post));
        }
        postDTOS = PostDTO.list(posts, PostDTO::withAuthor);

        return Response.ok(postDTOS).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("text")
    public Response createTextPost(@HeaderParam("Authorization") String auth, String content)
    throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext  ac          = authenticationFacade.authenticateBearerHeader(auth);
        ReceivedCreateTextPost post        = gson.fromJson(content, ReceivedCreateTextPost.class);
        Post                   createdPost = postFacade.createTextPost(ac, post.contents);
        return Response.status(CREATED).entity(gson.toJson(PostDTO.withAuthor(createdPost))).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("image")
    public Response createImagePost(@HeaderParam("Authorization") String auth, String content)
    throws ResourceNotFoundException, ImageThumbnailerException, UnsupportedImageFormatException, AuthenticationException
    {
        AuthenticationContext   ac   = authenticationFacade.authenticateBearerHeader(auth);
        ReceivedCreateImagePost post = gson.fromJson(content, ReceivedCreateImagePost.class);
        Post createdPost = postFacade.createImagePost(ac,
                                                      post.contents,
                                                      post.images);

        return Response.status(CREATED).entity(gson.toJson(PostDTO.withAuthor(createdPost))).build();
    }

    @GET
    @Path("{id: [0-9]+}")
    public Response getPostById(@PathParam("id") Integer id) throws ResourceNotFoundException
    {
        Post    post    = postFacade.get(id);
        PostDTO postDTO = new PostDTO(post);
        return Response.ok(postDTO).build();
    }

    @Path("timeline/{userId}/{pageSize}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimelinePosts(@PathParam("userId") Integer userId, @PathParam("pageSize") Integer pageSize, @QueryParam("cutoff") Integer cutoff) throws ResourceNotFoundException
    {
        List<Post>    posts    = postFacade.getTimelinePosts(userId, pageSize, cutoff);
        List<PostDTO> postDTOs = PostDTO.list(posts, PostDTO::withAuthor);
        return Response.ok(gson.toJson(postDTOs)).build();
    }


    @Path("user/{userId}/{pageSize}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRollingUserPosts(@PathParam("userId") Integer userId, @PathParam("pageSize") Integer pageSize, @QueryParam("cutoff") Integer cutoff)
    throws ResourceNotFoundException
    {
        List<Post>    posts    = postFacade.getRollingPostByUser(userId, pageSize, cutoff);
        List<PostDTO> postDTOs = PostDTO.list(posts, PostDTO::withAuthor);
        return Response.ok(gson.toJson(postDTOs)).build();
    }


    private class ReceivedCreateTextPost
    {
        private String contents;
    }

    private class ReceivedCreateImagePost
    {
        private String       contents;
        private List<String> images;
    }
}