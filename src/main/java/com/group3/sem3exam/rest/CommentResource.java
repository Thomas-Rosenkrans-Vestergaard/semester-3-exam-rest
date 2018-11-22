package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.group3.sem3exam.data.repositories.JpaCommentRepository;
import com.group3.sem3exam.data.repositories.JpaPostRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.CommentFacade;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("comments")
public class CommentResource
{
    private static Gson                          gson          = SpecializedGson.create();
    private static CommentFacade<JpaTransaction> commentFacade = new CommentFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaCommentRepository(transaction),
            transaction -> new JpaPostRepository(transaction)
    );

    @GET
    @Path("post/{post: [0-9]+}")
    public Response getByPost()
    {
        throw new UnsupportedOperationException("Hej Jonas");
    }
}
