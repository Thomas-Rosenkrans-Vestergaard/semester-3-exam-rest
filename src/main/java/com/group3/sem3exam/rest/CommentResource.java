package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.repositories.JpaCommentRepository;
import com.group3.sem3exam.logic.CityFacade;
import com.group3.sem3exam.logic.CommentFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.rest.dto.CommentDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

    @Path("comments")
    public class CommentResource
    {
        private static Gson          gson         = SpecializedGson.create();
        private static CommentFacade commentFacade = new CommentFacade(() -> new JpaCommentRepository(JpaConnection.create()));


        @GET
        @Path("{id: [0-9]+}")
        @Produces(APPLICATION_JSON)
        public Response getCityById() throws ResourceNotFoundException
        {
            return getCommentById();
        }

        @GET
        @Path("{id: [0-9]+}")
        @Produces(APPLICATION_JSON)
        public Response getCityById(@PathParam("id") int id) throws ResourceNotFoundException
        {
            Comment comment = CommentFacade.get(id);
            String  json    = gson.toJson(new CommentDTO());
            return Response.ok(json).build();
        }
    }
