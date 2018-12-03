package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.data.entities.ChatMessage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.ChatMessageRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.chat.messages.InMessage;
import com.group3.sem3exam.logic.chat.messages.InTextMessage;
import com.group3.sem3exam.logic.chat.messages.OutTextMessage;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChatFacade<T extends Transaction> implements ChatTransportInput
{

    private final ChatTransportOutput chatOutput;

    private final Supplier<T>                        transactionFactory;
    private final Function<T, ChatMessageRepository> messageRepositoryFactory;
    private final Function<T, UserRepository>        userRepositoryFactory;
    private final ChatMessageDelegator               delegator = new ChatMessageDelegator();

    public ChatFacade(
            ChatTransportOutput chatOutput,
            Supplier<T> transactionFactory,
            Function<T, ChatMessageRepository> messageRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory)
    {
        this.chatOutput = chatOutput;
        this.transactionFactory = transactionFactory;
        this.messageRepositoryFactory = messageRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;

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

    private void onTextMessage(AuthenticationContext sender, InTextMessage message)
    {
        try (ChatMessageRepository messageRepository = messageRepositoryFactory.apply(transactionFactory.get())) {
            chatOutput.send(OutTextMessage.of(message.getReceiver(), sender.getUser(), message.getContents()));
            messageRepository.write(sender.getUser(), message.getReceiver(), message.getContents());
        }
    }
}
