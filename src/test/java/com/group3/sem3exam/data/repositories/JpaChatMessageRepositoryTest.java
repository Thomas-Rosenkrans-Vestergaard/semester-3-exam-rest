package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.ChatMessage;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepositoryTester;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaChatMessageRepositoryTest
{


    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<ChatMessage, Integer, JpaChatMessageRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaChatMessageRepository(JpaTestConnection.create()),
                        (repository) -> createChatMessageMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, ChatMessage> createChatMessageMap(JpaChatMessageRepository chatMessageRepository)
    {
        JpaUserRepository userRepository = new JpaUserRepository(chatMessageRepository.getEntityManager());
        JpaCityRepository cityRepository = new JpaCityRepository(chatMessageRepository.getEntityManager());

        TreeMap<Integer, ChatMessage> map = new TreeMap<>();

        User sender   = TestingUtils.randomUser(userRepository, cityRepository.get(1));
        User receiver = TestingUtils.randomUser(userRepository, cityRepository.get(1));

        for (int i = 0; i < 5; i++) {
            ChatMessage chatMessage = chatMessageRepository.write(sender, receiver, "contents" + i);
            map.put(chatMessage.getId(), chatMessage);
        }

        return map;
    }

    @Test
    void write()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaChatMessageRepository messageRepository = new JpaChatMessageRepository(transaction);
            JpaUserRepository        userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository        cityRepository    = new JpaCityRepository(transaction);

            City city     = cityRepository.get(1);
            User sender   = TestingUtils.randomUser(userRepository, city);
            User receiver = TestingUtils.randomUser(userRepository, city);

            ChatMessage message = messageRepository.write(sender, receiver, "contents");

            assertEquals(sender, message.getSender());
            assertEquals(receiver, message.getReceiver());
            assertEquals("contents", message.getContents());
            assertFalse(message.getSeen());
        }
    }

    @Test
    void getHistory()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaChatMessageRepository messageRepository = new JpaChatMessageRepository(transaction);
            JpaUserRepository        userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository        cityRepository    = new JpaCityRepository(transaction);

            City city     = cityRepository.get(1);
            User sender   = TestingUtils.randomUser(userRepository, city);
            User receiver = TestingUtils.randomUser(userRepository, city);

            ChatMessage messageOne   = messageRepository.write(sender, receiver, "contents1");
            ChatMessage messageTwo   = messageRepository.write(receiver, sender, "contents2");
            ChatMessage messageThree = messageRepository.write(sender, receiver, "contents3");
            ChatMessage messageFour  = messageRepository.write(receiver, sender, "contents4");

            // Tests commutativity
            assertEquals(
                    messageRepository.getHistory(sender, receiver),
                    messageRepository.getHistory(receiver, sender)
            );

            List<ChatMessage> messages = messageRepository.getHistory(sender, receiver);
            assertEquals(4, messages.size());
            assertEquals(messageOne, messages.get(0));
            assertEquals(messageFour, messages.get(3));
        }
    }

    @Test
    void getHistoryRolling()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaChatMessageRepository messageRepository = new JpaChatMessageRepository(transaction);
            JpaUserRepository        userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository        cityRepository    = new JpaCityRepository(transaction);

            City city     = cityRepository.get(1);
            User sender   = TestingUtils.randomUser(userRepository, city);
            User receiver = TestingUtils.randomUser(userRepository, city);

            ChatMessage messageOne   = messageRepository.write(sender, receiver, "contents1");
            ChatMessage messageTwo   = messageRepository.write(receiver, sender, "contents2");
            ChatMessage messageThree = messageRepository.write(sender, receiver, "contents3");
            ChatMessage messageFour  = messageRepository.write(receiver, sender, "contents4");

            List<ChatMessage> results;

            // PageSize
            results = messageRepository.getHistory(sender, receiver, null, 3);
            assertEquals(3, results.size());
            assertEquals(messageTwo, results.get(0));

            // Without last
            results = messageRepository.getHistory(sender, receiver, null, 5);
            assertEquals(4, results.size());
            assertEquals(messageOne, results.get(0));

            // With last
            results = messageRepository.getHistory(sender, receiver, messageFour.getId(), 5);
            assertEquals(3, results.size());
            assertEquals(messageOne, results.get(0));
        }
    }

    @Test
    void countUnreadMessages()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaChatMessageRepository messageRepository = new JpaChatMessageRepository(transaction);
            JpaUserRepository        userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository        cityRepository    = new JpaCityRepository(transaction);

            City city     = cityRepository.get(1);
            User sender   = TestingUtils.randomUser(userRepository, city);
            User receiver = TestingUtils.randomUser(userRepository, city);

            messageRepository.write(sender, receiver, "contents1");

            Map<User, Integer> unread;

            unread = messageRepository.countUnreadMessages(receiver, Arrays.asList(sender));
            assertEquals(1, unread.size());
            assertEquals(1, (int) unread.get(sender));
        }
    }
}