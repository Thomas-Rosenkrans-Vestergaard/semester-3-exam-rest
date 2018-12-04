package com.group3.sem3exam.logic.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.group3.sem3exam.data.repositories.ChatMessageRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.authentication.TokenAuthenticator;
import com.group3.sem3exam.logic.chat.messages.InTextMessage;
import com.group3.sem3exam.logic.chat.messages.OutMessage;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Base64;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChatWebSocketServer<T extends Transaction> extends WebSocketServer implements ChatTransportOutput
{

    private       ChatTransportInput                 input;
    private final Supplier<T>                        transactionFactory;
    private final Function<T, ChatMessageRepository> messageRepositoryFactory;
    private final Function<T, UserRepository>        userRepositoryFactory;
    private final TokenAuthenticator                 tokenAuthenticator;
    private final Gson                               gson = new GsonBuilder().create();

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
        ChatConnection chatConnection = conn.getAttachment();
        if (chatConnection == null)
            conn.setAttachment(ChatConnectionData.random());

        input
    }

    private static class ChatConnectionData implements ChatConnection
    {
        private final String uniqueId;

        public ChatConnectionData(String uniqueId)
        {
            this.uniqueId = uniqueId;
        }

        public static ChatConnectionData random()
        {
            Random random  = new Random();
            byte   bytes[] = new byte[20];
            random.nextBytes(bytes);
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            String         token   = encoder.encodeToString(bytes);

            return new ChatConnectionData(token);
        }

        @Override
        public String getId()
        {
            return uniqueId;
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {

    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {
        JsonParser parser = new JsonParser();
        JsonObject root   = parser.parse(message).getAsJsonObject();
        String     type   = root.get("type").getAsString();

        if ("text".equals(type)) {
            input.onMessage(InTextMessage.of(
                    root.get("token").getAsString(),
                    conn.getAttachment(),
                    root.get("receiver").getAsInt(),
                    root.get("contents").getAsString()
            ));
        }

        throw new UnsupportedOperationException(type);
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
     * Sends the provided message to the provided user.
     *
     * @param message The message to send to the user.
     */
    @Override
    public void send(OutMessage message)
    {

    }
}
