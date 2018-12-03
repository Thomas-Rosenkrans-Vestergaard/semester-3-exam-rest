package com.group3.sem3exam.rest;

import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.chat.ChatFacade;
import com.group3.sem3exam.logic.chat.ChatMember;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("chat")
public class ChatResource
{

    private static ChatFacade<JpaTransaction> chatFacade           = Facades.chat;
    private static AuthenticationFacade       authenticationFacade = Facades.authentication;


    @GET
    @Path("history/{receiver: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getHistory(@HeaderParam("Authorization") String token, @PathParam("receiver") Integer receiver)
    {

    }


    @GET
    @Path("history/{receiver: [0-9]+}/{pageSize: [0-9]+}/{last}")
    @Produces(APPLICATION_JSON)
    public Response getHistoryPaginated(
            @HeaderParam("Authorization") String token,
            @PathParam("receiver") Integer receiver,
            @PathParam("pageSize") Integer pageSize,
            @PathParam("last") Integer last)
    {
        return Response.ok().build();
    }

    @GET
    @Path("users")
    @Produces(APPLICATION_JSON)
    public Response getRegistry(@HeaderParam("Authorization") String token) throws AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        List<ChatMember>      chatMembers           = chatFacade.getUsers(authenticationContext);
        
    }
}
