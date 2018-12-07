package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.ChatMessage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.CrudRepository;

import java.util.List;
import java.util.Map;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer>
{

    /**
     * Creates a new chat message.
     *
     * @param sender   The sender of the message.
     * @param receiver The receiver of the message.
     * @param contents The contents of the message.
     * @return The newly created chat message.
     */
    ChatMessage write(User sender, User receiver, String contents);

    /**
     * Returns the complete history of chat messages between the two users.
     *
     * @param one The first user.
     * @param two The second user.
     * @return The chat history between the two users. The list is returned in an ascending order, with
     * the oldest messages first.
     */
    List<ChatMessage> getHistory(User one, User two);

    /**
     * Returns a paginated view of the chat messages between the two users.
     * <p>
     * The pagination system works by retrieving a subset of the history between the two users. The {@code last} variable
     * indicated the starts of the result set.
     *
     * @param one      The first user.
     * @param two      The second user.
     * @param last     The id of the last message retrieved. Only chat messages with {@code chatMessage.id < last} are
     *                 retrieved. When {@code last == null} the last newest message is instead used.
     * @param pageSize The number of results to retrieve. Where {@code pageSize > 0}.
     * @return The chat history. The list is returned in an ascending order, with the oldest messages first.
     */
    List<ChatMessage> getHistory(User one, User two, Integer last, Integer pageSize);

    /**
     * Counts the unread messages from the list of users send to {@code self}.
     *
     * @param self    The user to count the unread messages of.
     * @param friends The senders of the unread messages.
     * @return A map containing a provided user and the number of unread messages sent by that user.
     */
    Map<User, Integer> countUnreadMessages(User self, List<User> friends);
}
