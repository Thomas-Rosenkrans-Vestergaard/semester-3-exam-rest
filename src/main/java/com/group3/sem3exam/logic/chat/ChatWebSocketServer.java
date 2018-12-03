package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.ChatMessageRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.TokenAuthenticator;
import com.group3.sem3exam.logic.chat.messages.InMessage;
import com.group3.sem3exam.logic.chat.messages.OutMessage;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChatWebSocketServer<T extends Transaction> extends WebSocketServer implements ChatTransportOutput, ChatTransportInput
{

    private       ChatTransportInput                 input;
    private       Map<Integer, WebSocket>            connections      = new HashMap<>();
    private final Supplier<T>                        transactionFactory;
    private final Function<T, ChatMessageRepository> messageRepositoryFactory;
    private final Function<T, UserRepository>        userRepositoryFactory;
    private final TokenAuthenticator                 tokenAuthenticator;

    public ChatWebSocketServer(
            Supplier<T> transactionFactory,
            Function<T, ChatMessageRepository> messageRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory,
            TokenAuthenticator tokenAuthenticator)
    {
        super(new InetSocketAddress(8887));
        this.transactionFactory = transactionFactory;
        this.messageRepositoryFactory = messageRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.tokenAuthenticator = tokenAuthenticator;
    }

    public void setInput(ChatTransportInput input)
    {
        this.input = input;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake)
    {

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {

    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {

    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {

    }

    @Override
    public void onStart()
    {
        System.out.println("ChatServer started.");
    }

    /**
     * Called when a message is received by the chat server.
     *
     * @param authenticationContext The authentication context of the closed connection.
     * @param receiver              The receiver of the message.
     * @param message               The message sent by the client.
     */
    @Override
    public void onMessage(AuthenticationContext authenticationContext, User receiver, InMessage message)
    {

    }

    /**
     * Sends the provided message to the provided user.
     *
     * @param message The message to send to the user.
     */
    @Override
    public void send(OutMessage message)
    {

    }
}
