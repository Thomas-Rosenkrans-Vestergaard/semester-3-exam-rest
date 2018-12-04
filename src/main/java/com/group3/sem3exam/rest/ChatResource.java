package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.ChatMessage;
import com.group3.sem3exam.data.repositories.JpaChatMessageRepository;
import com.group3.sem3exam.data.repositories.JpaFriendshipRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.JpaServiceRepository;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.chat.ChatFacade;
import com.group3.sem3exam.logic.chat.ChatMember;
import com.group3.sem3exam.logic.chat.ChatWebSocketServer;
import com.group3.sem3exam.rest.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("chat")
public class ChatResource
{

    private static ChatFacade<JpaTransaction> chatFacade = ServletContextClass.chatFacade;
    private static AuthenticationFacade       authenticationFacade = Facades.authentication;
    private static Gson                       gson                 = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Path("history/{friend: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getHistory(@HeaderParam("Authorization") String token, @PathParam("friend") Integer friend)
    {
        return Response.ok().build();
    }


    @GET
    @Path("history/{friend: [0-9]+}/{pageSize: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getHistoryPaginated(
            @HeaderParam("Authorization") String token,
            @PathParam("friend") Integer friend,
            @PathParam("pageSize") Integer pageSize,
            @QueryParam("last") Integer last) throws AuthenticationException, ResourceNotFoundException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        List<ChatMessage>     messages              = chatFacade.getHistory(authenticationContext, friend, last, pageSize);
        return Response.ok(gson.toJson(ChatMessageDTO.map(messages, ChatMessageDTO::withoutUsers))).build();
    }

    private static class ChatMessageDTO
    {

        private Integer id;
        private String  contents;
        private UserDTO sender;
        private UserDTO receiver;

        public ChatMessageDTO(Integer id, String contents, UserDTO sender, UserDTO receiver)
        {
            this.id = id;
            this.contents = contents;
            this.sender = sender;
            this.receiver = receiver;
        }

        public static ChatMessageDTO withoutUsers(ChatMessage message)
        {
            return new ChatMessageDTO(
                    message.getId(),
                    message.getContents(),
                    null,
                    null
            );
        }

        public static List<ChatMessageDTO> map(List<ChatMessage> messages, Function<ChatMessage, ChatMessageDTO> f)
        {
            List<ChatMessageDTO> results = new ArrayList<>();
            for (ChatMessage message : messages)
                results.add(f.apply(message));

            return results;
        }
    }

    @GET
    @Path("friends")
    @Produces(APPLICATION_JSON)
    public Response getRegistry(@HeaderParam("Authorization") String token) throws AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(token);
        List<ChatMember>      chatMembers           = chatFacade.getUsers(authenticationContext);
        return Response.ok(gson.toJson(ChatMemberDTO.map(chatMembers, ChatMemberDTO::with))).build();
    }

    private static class ChatMemberDTO
    {
        private final UserDTO user;
        private final int     unreadMessages;
        private final boolean online;

        public ChatMemberDTO(UserDTO user, int unreadMessages, boolean online)
        {
            this.user = user;
            this.unreadMessages = unreadMessages;
            this.online = online;
        }

        public static ChatMemberDTO with(ChatMember chatMember)
        {
            return new ChatMemberDTO(UserDTO.publicView(chatMember.getUser()), chatMember.unreadMessages(), chatMember.isOnline());
        }

        public static List<ChatMemberDTO> map(List<ChatMember> members, Function<ChatMember, ChatMemberDTO> f)
        {
            List<ChatMemberDTO> results = new ArrayList<>(members.size());
            for (ChatMember member : members)
                results.add(f.apply(member));

            return results;
        }
    }
}
