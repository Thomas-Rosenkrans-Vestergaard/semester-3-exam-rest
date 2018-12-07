package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.ChatMessage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.*;

public class JpaChatMessageRepository extends JpaCrudRepository<ChatMessage, Integer> implements ChatMessageRepository
{

    /**
     * Creates a new {@link JpaChatMessageRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaChatMessageRepository(EntityManager entityManager)
    {
        super(entityManager, ChatMessage.class);
    }

    /**
     * Creates a new {@link JpaChatMessageRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaChatMessageRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, ChatMessage.class);
    }

    /**
     * Creates a new {@link JpaChatMessageRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaChatMessageRepository(JpaTransaction transaction)
    {
        super(transaction, ChatMessage.class);
    }

    @Override
    public ChatMessage write(User sender, User receiver, String contents)
    {
        ChatMessage chatMessage = new ChatMessage(sender, receiver, contents);
        getEntityManager().persist(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessage> getHistory(User one, User two)
    {
        return getEntityManager()
                .createQuery("SELECT cm FROM ChatMessage cm " +
                             "WHERE (cm.sender = :one AND cm.receiver = :two) " +
                             "OR (cm.sender = :two AND cm.receiver = :one) " +
                             "ORDER BY cm.id ASC", ChatMessage.class)
                .setParameter("one", one)
                .setParameter("two", two)
                .getResultList();
    }

    @Override
    public List<ChatMessage> getHistory(User one, User two, Integer last, Integer pageSize)
    {
        last = last == null ? Integer.MAX_VALUE : last;
        pageSize = Math.max(pageSize, 1);

        List<ChatMessage> messages = getEntityManager()
                .createQuery("SELECT cm FROM ChatMessage cm " +
                             "WHERE ((cm.sender = :one AND cm.receiver = :two) " +
                             "OR (cm.sender = :two AND cm.receiver = :one)) " +
                             "AND cm.id < :last " +
                             "ORDER BY cm.id DESC", ChatMessage.class)
                .setParameter("one", one)
                .setParameter("two", two)
                .setParameter("last", last)
                .setMaxResults(pageSize)
                .getResultList();

        Collections.sort(messages, Comparator.comparingInt(ChatMessage::getId));
        return messages;
    }

    @Override
    public Map<User, Integer> countUnreadMessages(User self, List<User> friends)
    {
        List<CountUnreadResult> list = getEntityManager()
                .createQuery("SELECT new com.group3.sem3exam.data.repositories.JpaChatMessageRepository$CountUnreadResult(" +
                             "u, " +
                             "(SELECT count(cm) FROM ChatMessage cm WHERE cm.seen = false AND cm.sender = u AND cm.receiver = :self)) " +
                             "FROM User u WHERE u IN :friends", CountUnreadResult.class)
                .setParameter("friends", friends)
                .setParameter("self", self)
                .getResultList();

        Map<User, Integer> map = new HashMap<>();
        for (CountUnreadResult result : list)
            map.put(result.user, result.count);

        return map;
    }

    public static class CountUnreadResult
    {
        public final User    user;
        public final Integer count;

        public CountUnreadResult(User user, Long count)
        {
            this.user = user;
            this.count = (int) (long) count;
        }
    }

    @Override
    public ChatMessage markSeen(ChatMessage message)
    {
        message.setSeen(true);
        update(message);
        return message;
    }
}
