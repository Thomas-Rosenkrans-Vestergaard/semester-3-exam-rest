package com.group3.sem3exam.rest;

import com.group3.sem3exam.data.repositories.JpaChatMessageRepository;
import com.group3.sem3exam.data.repositories.JpaFriendshipRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.JpaServiceRepository;
import com.group3.sem3exam.logic.chat.ChatFacade;
import com.group3.sem3exam.logic.chat.ChatWebSocketServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextClass implements ServletContextListener
{
    public static ChatFacade chatFacade = null;

    public void contextInitialized(ServletContextEvent arg0)
    {
        ChatWebSocketServer<JpaTransaction> chatWebSocketServer = new ChatWebSocketServer<>(
                () -> new JpaTransaction(JpaConnection.create()),
                transaction -> new JpaChatMessageRepository(transaction),
                transaction -> new JpaUserRepository(transaction),
                null
        );

        chatFacade = new ChatFacade<>(chatWebSocketServer,
                                      () -> new JpaTransaction(JpaConnection.create()),
                                      transaction -> new JpaChatMessageRepository(transaction),
                                      transaction -> new JpaUserRepository(transaction),
                                      transaction -> new JpaFriendshipRepository(transaction),
                                      transaction -> new JpaServiceRepository(transaction),
                                      Facades.jwtSecret
        );

        chatWebSocketServer.setInputTransport(chatFacade);
        chatWebSocketServer.start();
    }
}