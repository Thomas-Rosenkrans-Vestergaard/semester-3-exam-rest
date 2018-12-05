package com.group3.sem3exam.logic.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.group3.sem3exam.data.repositories.ChatMessageRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.authentication.TokenAuthenticator;
import com.group3.sem3exam.logic.chat.messages.AuthenticationMessage;
import com.group3.sem3exam.logic.chat.messages.InTextMessage;
import com.group3.sem3exam.logic.chat.messages.OutMessage;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChatWebSocketServer<T extends Transaction> extends WebSocketServer implements ChatTransportOutput
{

    private       ChatTransportInput                 inputTransport;
    private final Supplier<T>                        transactionFactory;
    private final Function<T, ChatMessageRepository> messageRepositoryFactory;
    private final Function<T, UserRepository>        userRepositoryFactory;
    private final TokenAuthenticator                 tokenAuthenticator;
    private final Gson                               gson    = new GsonBuilder().create();
    private final Map<ChatConnection, WebSocket>     sockets = new HashMap<>();

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

    public void setInputTransport(ChatTransportInput inputTransport)
    {
        this.inputTransport = inputTransport;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake)
    {
        ChatConnection chatConnection = conn.getAttachment();
        if (chatConnection == null) {
            chatConnection = ChatConnectionData.random();
            conn.setAttachment(chatConnection);
        }

        this.sockets.put(chatConnection, conn);
        this.inputTransport.onConnect(chatConnection);
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

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof ChatConnectionData)) return false;
            ChatConnectionData that = (ChatConnectionData) o;
            return Objects.equals(uniqueId, that.uniqueId);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(uniqueId);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {
        this.inputTransport.onClose(conn.getAttachment());
    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {
        JsonParser parser = new JsonParser();
        JsonObject root   = parser.parse(message).getAsJsonObject();
        String     type   = root.get("type").getAsString();

        if ("text".equals(type)) {
            inputTransport.onMessage(InTextMessage.of(
                    conn.getAttachment(),
                    root.get("receiver").getAsInt(),
                    root.get("contents").getAsString()
            ));
            return;
        }

        if ("authentication".equals(type)) {
            inputTransport.onMessage(AuthenticationMessage.of(
                    root.get("token").getAsString(),
                    conn.getAttachment()
            ));
            return;
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
     * @param receiver The connection the message should be sent to.
     * @param message  The message to send to the user.
     */
    @Override
    public void send(ChatConnection receiver, OutMessage message)
    {
        WebSocket socket = this.sockets.get(receiver);
        socket.send(createJson(message));
    }

    private String createJson(OutMessage message)
    {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Root root = new Root();
        root.type = message.getType();
        root.payload = message.getPayload();
        return gson.toJson(root);
    }

    private class Root
    {
        public String         type;
        public Map<String, ?> payload;
    }
}
