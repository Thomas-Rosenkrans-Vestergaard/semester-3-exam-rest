package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.ChatMessage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaReadRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class JpaChatMessageRepository extends JpaReadRepository<ChatMessage, Integer> implements ChatMessageRepository
{

    public JpaChatMessageRepository(EntityManager entityManager)
    {
        super(entityManager, ChatMessage.class);
    }

    public JpaChatMessageRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, ChatMessage.class);
    }

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
                             "ORDER BY cm.id DESC", ChatMessage.class)
                .setParameter("one", one)
                .setParameter("two", two)
                .getResultList();
    }

    @Override
    public List<ChatMessage> getHistory(User one, User two, Integer last, Integer pageSize)
    {
        last = last == null ? Integer.MAX_VALUE : last;
        pageSize = Math.max(pageSize, 1);

        return getEntityManager()
                .createQuery("SELECT cm FROM ChatMessage cm " +
                             "WHERE (cm.sender = :one AND cm.receiver = :two) " +
                             "OR (cm.sender = :two AND cm.receiver = :one) " +
                             "AND cm.id < :last " +
                             "ORDER BY cm.id DESC", ChatMessage.class)
                .setParameter("one", one)
                .setParameter("two", two)
                .setParameter("last", last)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
