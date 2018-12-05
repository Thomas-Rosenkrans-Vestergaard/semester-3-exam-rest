package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.*;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.images.ImageThumbnailerException;
import com.group3.sem3exam.logic.images.UnsupportedImageFormatException;
import com.group3.sem3exam.rest.dto.PostDTO;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.group3.sem3exam.rest.Facades.post;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("posts")
public class PostResource
{
    private static Gson                       gson                 = SpecializedGson.create();
    private static PostFacade<JpaTransaction> postFacade           = post;
    private static AuthenticationFacade       authenticationFacade = Facades.authentication;
    private static CommentFacade              commentFacade        = Facades.comments;


    @GET
    @Path("user/{userId: [0-9]+}")
    public Response getPostByUser(@PathParam("userId") Integer id) throws ResourceNotFoundException
    {
        List<Post>    posts    = postFacade.getPostByUser(id);
        List<PostDTO> postDTOS = PostDTO.list(posts, PostDTO::withAuthor);
        return Response.ok(gson.toJson(postDTOS)).build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response createPost(@HeaderParam("Authorization") String auth, String content)
    throws AuthenticationException, ImageThumbnailerException, UnsupportedImageFormatException
    {
        AuthenticationContext  ac          = authenticationFacade.authenticateBearerHeader(auth);
        ReceivedCreateTextPost post        = gson.fromJson(content, ReceivedCreateTextPost.class);
        Post                   createdPost = postFacade.createPost(ac, post.contents, post.images == null ? new ArrayList<>() : post.images);
        return Response.status(CREATED).entity(gson.toJson(PostDTO.withAuthor(createdPost))).build();
    }

    @GET
    @Path("{id: [0-9]+}")
    public Response getPostById(@PathParam("id") Integer id) throws ResourceNotFoundException
    {
        Post    post    = postFacade.get(id);
        PostDTO postDTO = PostDTO.withAuthor(post);
        return Response.ok(postDTO).build();
    }

    @Path("timeline/{userId}/{pageSize}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getTimelinePosts(@PathParam("userId") Integer userId, @PathParam("pageSize") Integer pageSize, @QueryParam("cutoff") Integer cutoff)
    {
        List<Post>    posts    = postFacade.getTimelinePosts(userId, pageSize, cutoff);
        List<PostDTO> postDTOs = PostDTO.list(posts, PostDTO::withAuthor);
        return Response.ok(gson.toJson(postDTOs)).build();
    }

    @Path("user/{userId}/{pageSize}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getRollingUserPosts(@PathParam("userId") Integer userId, @PathParam("pageSize") Integer pageSize, @QueryParam("cutoff") Integer cutoff)
    throws ResourceNotFoundException
    {
        List<Post>    posts    = postFacade.getRollingPostByUser(userId, pageSize, cutoff);
        List<PostDTO> postDTOs = PostDTO.list(posts, PostDTO::withAuthor);
        return Response.ok(gson.toJson(postDTOs)).build();
    }


    @DELETE
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response deletePost(@HeaderParam("authorization") String token, @PathParam("id") Integer id) throws ResourceNotFoundException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        Post                  post                  = postFacade.delete(authenticationContext, id);
        return Response.ok(gson.toJson(PostDTO.basic(post))).build();
    }




    private class ReceivedCreateTextPost
    {
        private String                     contents;
        private List<ImageDeclarationImpl> images = new ArrayList<>();
    }

    private class ImageDeclarationImpl implements PostFacade.ImageDeclaration
    {

        public String data;
        public String description;

        public ImageDeclarationImpl()
        {
        }

        @Override
        public String getData()
        {
            return data;
        }

        @Override
        public String getDescription()
        {
            return description;
        }
    }
}