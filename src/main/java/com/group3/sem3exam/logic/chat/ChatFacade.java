package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.data.entities.ChatMessage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.ChatMessageRepository;
import com.group3.sem3exam.data.repositories.FriendshipRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.data.services.ServiceRepository;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.TokenAuthenticator;
import com.group3.sem3exam.logic.authentication.jwt.JwtSecret;
import com.group3.sem3exam.logic.chat.messages.InMessage;
import com.group3.sem3exam.logic.chat.messages.InTextMessage;
import com.group3.sem3exam.logic.chat.messages.OutTextMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChatFacade<T extends Transaction> implements ChatTransportInput, Chat
{

    private final ChatTransportOutput chatOutput;

    private final Supplier<T>                         transactionFactory;
    private final Function<T, ChatMessageRepository>  messageRepositoryFactory;
    private final Function<T, UserRepository>         userRepositoryFactory;
    private final Function<T, FriendshipRepository>   friendshipRepositoryFactory;
    private final Function<T, ServiceRepository>      serviceRepositoryFactory;
    private final ChatMessageDelegator                delegator          = new ChatMessageDelegator();
    private final Map<Integer, AuthenticationContext> authenticatedUsers = new HashMap<>();
    private final Map<String, ChatConnection>         connections        = new HashMap<>();
    private final JwtSecret                           jwtSecret;

    public ChatFacade(
            ChatTransportOutput chatOutput,
            Supplier<T> transactionFactory,
            Function<T, ChatMessageRepository> messageRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory,
            Function<T, FriendshipRepository> friendshipRepositoryFactory,
            Function<T, ServiceRepository> serviceRepositoryFactory,
            JwtSecret jwtSecret)
    {
        this.chatOutput = chatOutput;
        this.transactionFactory = transactionFactory;
        this.messageRepositoryFactory = messageRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.serviceRepositoryFactory = serviceRepositoryFactory;
        this.friendshipRepositoryFactory = friendshipRepositoryFactory;
        this.jwtSecret = jwtSecret;

        delegator.register(InTextMessage.class, this::onTextMessage);
    }

    /**
     * Returns the complete history of chat messages between the authenticated user and the user with the
     * provided id {@code receiver}.
     *
     * @param authenticated The authenticated user.
     * @param receiver      The id of the receiver.
     * @return The chat history between the two chatUsers. The list is returned in an ascending order, with
     * the oldest messages first.
     * @throws ResourceNotFoundException When the provided receiver does not exist.
     */
    public List<ChatMessage> getHistory(AuthenticationContext authenticated, Integer receiver) throws ResourceNotFoundException
    {
        // TODO: Add authorization

        try (T transaction = transactionFactory.get()) {
            ChatMessageRepository messageRepository = messageRepositoryFactory.apply(transaction);
            UserRepository        userRepository    = userRepositoryFactory.apply(transaction);

            User fetchedReceiver = userRepository.get(receiver);
            if (fetchedReceiver == null)
                throw new ResourceNotFoundException(User.class, receiver);

            return messageRepository.getHistory(authenticated.getUser(), fetchedReceiver);
        }
    }

    /**
     * Returns a paginated view of the chat messages between the two chatUsers.
     * <p>
     * The pagination system works by retrieving a subset of the history between the two chatUsers. The {@code last} variable
     * indicated the starts of the result set.
     *
     * @param authenticated The authenticated user.
     * @param receiver      The id of the receiver.
     * @param last          The id of the last message retrieved. Only chat messages with {@code chatMessage.id < last} are
     *                      retrieved. When {@code last == null} the last newest message is instead used.
     * @param pageSize      The number of results to retrieve. Where {@code pageSize > 0}.
     * @return The chat history. The list is returned in an ascending order, with the oldest messages first.
     * @throws ResourceNotFoundException When the provided receiver does not exist.
     */
    public List<ChatMessage> getHistory(AuthenticationContext authenticated, Integer receiver, Integer last, Integer pageSize)
    throws ResourceNotFoundException
    {
        // TODO: Add authorization

        last = last == null ? Integer.MAX_VALUE : last;

        try (T transaction = transactionFactory.get()) {
            ChatMessageRepository messageRepository = messageRepositoryFactory.apply(transaction);
            UserRepository        userRepository    = userRepositoryFactory.apply(transaction);

            User fetchedReceiver = userRepository.get(receiver);
            if (fetchedReceiver == null)
                throw new ResourceNotFoundException(User.class, receiver);

            return messageRepository.getHistory(authenticated.getUser(), fetchedReceiver, last, pageSize);
        }
    }

    public List<ChatMember> getUsers(AuthenticationContext auth)
    {
        List<ChatMember> result = new ArrayList<>();
        try (FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transactionFactory.get())) {
            List<User> friends = friendshipRepository.getFriends(auth.getUserId());
            for (User friend : friends)
                result.add(new ChatMemberData(friend, isOnline(friend), -1));

            return result;
        }
    }

    private class ChatMemberData implements ChatMember
    {

        private final User    user;
        private final boolean isOnline;
        private final int     unreadMessages;

        public ChatMemberData(User user, boolean isOnline, int unreadMessages)
        {
            this.user = user;
            this.isOnline = isOnline;
            this.unreadMessages = unreadMessages;
        }

        /**
         * Returns the user.
         *
         * @return The user.
         */
        @Override
        public User getUser()
        {
            return user;
        }

        public boolean isOnline()
        {
            return this.isOnline;
        }

        /**
         * Returns the number of unread messages by the user.
         *
         * @return The number of unread messages by the user.
         */
        @Override
        public int unreadMessages()
        {
            return unreadMessages;
        }
    }

    /**
     * Called when a message is received by the chat server.
     *
     * @param message The message sent by the client.
     */
    @Override
    public void onMessage(InMessage message)
    {
        try {
            authenticate(message);
            this.delegator.handle(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AuthenticationContext authenticate(InMessage message) throws AuthenticationException
    {
        String id = message.getSender().getId();
        if (this.authenticatedUsers.containsKey(id))
            return authenticatedUsers.get(id);

        try (T transaction = transactionFactory.get()) {
            UserRepository    userRepository    = userRepositoryFactory.apply(transaction);
            ServiceRepository serviceRepository = serviceRepositoryFactory.apply(transaction);
            TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(jwtSecret,
                                                                           () -> userRepository,
                                                                           () -> serviceRepository);

            AuthenticationContext authenticationContext = tokenAuthenticator.authenticate(message.getAuthToken());
            this.authenticatedUsers.put(authenticationContext.getUserId(), authenticationContext);
            return authenticationContext;
        }
    }

    private User getAuthenticatedUser(ChatConnection connection)
    {
        return authenticatedUsers.get(connection.getId()).getUser();
    }

    private void onTextMessage(InTextMessage message)
    {
        try (T transaction = transactionFactory.get()) {
            ChatMessageRepository messageRepository = messageRepositoryFactory.apply(transaction);
            UserRepository        userRepository    = userRepositoryFactory.apply(transaction);

            ChatConnection receiverConnection = connections.get(message.getSender().getId());
            if (receiverConnection != null)
                chatOutput.send(OutTextMessage.of(receiverConnection, message.getSender(), message.getContents()));

            User sender   = getAuthenticatedUser(message.getSender());
            User receiver = userRepository.get(message.getReceiver());
            messageRepository.write(sender, receiver, message.getContents());
        }
    }

    @Override
    public boolean isOnline(User user)
    {
        return this.authenticatedUsers.containsKey(user.getId());
    }
}
