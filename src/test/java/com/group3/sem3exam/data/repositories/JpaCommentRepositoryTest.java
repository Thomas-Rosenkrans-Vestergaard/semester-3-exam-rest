package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaCommentRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<Comment, Integer, JpaCommentRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaCommentRepository(JpaTestConnection.create()),
                        (repository) -> createCommentMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, Comment> createCommentMap(JpaCommentRepository commentRepository)
    {
        TreeMap<Integer, Comment> map = new TreeMap<>();

        JpaPostRepository postRepository = new JpaPostRepository(commentRepository.getEntityManager());
        JpaUserRepository userRepository = new JpaUserRepository(commentRepository.getEntityManager());
        JpaCityRepository cityRepository = new JpaCityRepository(commentRepository.getEntityManager());

        City city = cityRepository.get(1);
        User user = TestingUtils.randomUser(userRepository, city);
        Post post = TestingUtils.randomPost(postRepository, user);

        for (int i = 0; i < 5; i++) {
            Comment comment = commentRepository.create(user, "contents" + i, post);
            map.put(comment.getId(), comment);
        }

        return map;
    }


    @Test
    void create()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository    postRepository    = new JpaPostRepository(transaction);
            JpaUserRepository    userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository    cityRepository    = new JpaCityRepository(transaction);
            JpaCommentRepository commentRepository = new JpaCommentRepository(transaction);

            City city = cityRepository.get(1);
            User user = TestingUtils.randomUser(userRepository, city);
            Post post = TestingUtils.randomPost(postRepository, user);

            Comment comment = commentRepository.create(user, "contents", post);

            assertNotNull(comment.getId());
            assertEquals(user, comment.getAuthor());
            assertEquals("contents", comment.getContents());
            assertEquals(post, comment.getParent());
            assertTrue(LocalDateTime.now().minusMinutes(1).isBefore(comment.getCreatedAt()));
        }
    }

    @Test
    void getParent()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository    postRepository    = new JpaPostRepository(transaction);
            JpaUserRepository    userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository    cityRepository    = new JpaCityRepository(transaction);
            JpaCommentRepository commentRepository = new JpaCommentRepository(transaction);

            City city = cityRepository.get(1);
            User user = TestingUtils.randomUser(userRepository, city);
            Post post = TestingUtils.randomPost(postRepository, user);

            assertNull(commentRepository.getParent(-1));
            assertEquals(post, commentRepository.getParent(post.getId()));
        }
    }

    @Test
    void parentExists()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository    postRepository    = new JpaPostRepository(transaction);
            JpaUserRepository    userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository    cityRepository    = new JpaCityRepository(transaction);
            JpaCommentRepository commentRepository = new JpaCommentRepository(transaction);

            City city = cityRepository.get(1);
            User user = TestingUtils.randomUser(userRepository, city);
            Post post = TestingUtils.randomPost(postRepository, user);

            assertFalse(commentRepository.parentExists(-1));
            assertTrue(commentRepository.parentExists(post.getId()));
        }
    }

    @Test
    void getAll()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository    postRepository    = new JpaPostRepository(transaction);
            JpaUserRepository    userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository    cityRepository    = new JpaCityRepository(transaction);
            JpaCommentRepository commentRepository = new JpaCommentRepository(transaction);

            City city = cityRepository.get(1);
            User user = TestingUtils.randomUser(userRepository, city);
            Post post = TestingUtils.randomPost(postRepository, user);

            assertEquals(0, commentRepository.getAll(post).size());
            Comment comment = commentRepository.create(user, "contents", post);
            assertEquals(1, commentRepository.getAll(post).size());
            assertEquals(comment, commentRepository.getAll(post).get(0));
        }
    }

    @Test
    void getPaginated()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository    postRepository    = new JpaPostRepository(transaction);
            JpaUserRepository    userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository    cityRepository    = new JpaCityRepository(transaction);
            JpaCommentRepository commentRepository = new JpaCommentRepository(transaction);

            City city = cityRepository.get(1);
            User user = TestingUtils.randomUser(userRepository, city);
            Post post = TestingUtils.randomPost(postRepository, user);

            Comment commentOne   = commentRepository.create(user, "contents1", post);
            Comment commentTwo   = commentRepository.create(user, "contents2", post);
            Comment commentThree = commentRepository.create(user, "contents3", post);

            List<Comment> results;

            // PageSize
            results = commentRepository.getPaginated(post, 2, 1);
            assertEquals(2, results.size());
            assertEquals(commentOne, results.get(0));

            // PageNumber
            results = commentRepository.getPaginated(post, 2, 2);
            assertEquals(1, results.size());
            assertEquals(commentThree, results.get(0));
        }
    }

    @Test
    void count()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository    postRepository    = new JpaPostRepository(transaction);
            JpaUserRepository    userRepository    = new JpaUserRepository(transaction);
            JpaCityRepository    cityRepository    = new JpaCityRepository(transaction);
            JpaCommentRepository commentRepository = new JpaCommentRepository(transaction);

            City city = cityRepository.get(1);
            User user = TestingUtils.randomUser(userRepository, city);
            Post post = TestingUtils.randomPost(postRepository, user);

            assertEquals(0, commentRepository.count(post));
            commentRepository.create(user, "contents1", post);
            assertEquals(1, commentRepository.count(post));
            commentRepository.create(user, "contents2", post);
            assertEquals(2, commentRepository.count(post));
        }
    }
}