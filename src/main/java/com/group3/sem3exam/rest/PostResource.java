package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.repositories.JpaImagePostImageRepository;
import com.group3.sem3exam.data.repositories.JpaImageRepository;
import com.group3.sem3exam.data.repositories.JpaPostRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.PostFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
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
    private static Gson                       gson       = SpecializedGson.create();
    private static PostFacade<JpaTransaction> postFacade = new PostFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaPostRepository(transaction),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaImagePostImageRepository(transaction)
    );

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
    public Response createTextPost(String content) throws ResourceNotFoundException
    {
        ReceivedCreateTextPost post = gson.fromJson(content, ReceivedCreateTextPost.class);
        Post createdPost = postFacade.createTextPost(post.title,
                                                 post.contents,
                                                 post.author);
        return Response.status(CREATED).entity(gson.toJson(PostDTO.basic(createdPost))).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("image")
    public Response createImagePost(String content) throws ResourceNotFoundException, ImageThumbnailerException, UnsupportedImageFormatException
    {
        ReceivedCreateImagePost post = gson.fromJson(content, ReceivedCreateImagePost.class);
        Post createdPost = postFacade.createImagePost(post.title,
                                                     post.contents,
                                                     post.author,
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
    private String title;
    private Integer author;
    }

    private class ReceivedCreateImagePost
    {
        private String contents;
        private String title;
        private Integer author;
        private List<String> images;
    }


}