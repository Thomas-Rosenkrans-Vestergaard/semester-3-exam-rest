package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaReadRepositoryTester;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JpaPostRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaReadRepositoryTester<Post, Integer, JpaPostRepository> tester =
                new JpaReadRepositoryTester<>(
                        () -> new JpaPostRepository(JpaTestConnection.create()),
                        (repository) -> createPostMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, Post> createPostMap(JpaPostRepository postRepository)
    {
        TreeMap<Integer, Post> map = new TreeMap<>();

        JpaCityRepository cityRepository = new JpaCityRepository(postRepository.getEntityManager());
        JpaUserRepository userRepository = new JpaUserRepository(postRepository.getEntityManager());
        User              user           = TestingUtils.randomUser(userRepository, cityRepository.get(1));

        for (int i = 0; i < 5; i++) {
            Post post = postRepository.create(user, "contents" + (i + 1), new ArrayList<>(), LocalDateTime.now().minusDays(i + 5));
            map.put(post.getId(), post);
        }

        return map;
    }

    @Test
    void create()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository postRepository = new JpaPostRepository(transaction);
            JpaUserRepository userRepository = new JpaUserRepository(transaction);
            JpaCityRepository cityRepository = new JpaCityRepository(transaction);

            City          city = cityRepository.get(1);
            User          user = TestingUtils.randomUser(userRepository, city);
            LocalDateTime time = LocalDateTime.now();
            Post          post = postRepository.create(user, "body", new ArrayList<>(), time);
            assertEquals(user, post.getAuthor());
            assertEquals("body", post.getContents());
            assertTrue(post.getImages().isEmpty());
            assertEquals(time, post.getCreatedAt());
        }
    }

    @Test
    void getByAuthor()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository postRepository = new JpaPostRepository(transaction);
            JpaUserRepository userRepository = new JpaUserRepository(transaction);
            JpaCityRepository cityRepository = new JpaCityRepository(transaction);

            City city    = cityRepository.get(1);
            User user    = TestingUtils.randomUser(userRepository, city);
            Post postOne = postRepository.create(user, "body1", new ArrayList<>(), LocalDateTime.now());
            Post postTwo = postRepository.create(user, "body2", new ArrayList<>(), LocalDateTime.now());

            List<Post> results = postRepository.getByAuthor(user);
            assertEquals(2, results.size());
            assertEquals(postOne, results.get(0));
            assertEquals(postTwo, results.get(1));
        }
    }

    @Test
    void getByAuthorRolling()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository postRepository = new JpaPostRepository(transaction);
            JpaUserRepository userRepository = new JpaUserRepository(transaction);
            JpaCityRepository cityRepository = new JpaCityRepository(transaction);

            City city      = cityRepository.get(1);
            User user      = TestingUtils.randomUser(userRepository, city);
            Post postOne   = postRepository.create(user, "body1", new ArrayList<>(), LocalDateTime.now());
            Post postTwo   = postRepository.create(user, "body2", new ArrayList<>(), LocalDateTime.now());
            Post postThree = postRepository.create(user, "body3", new ArrayList<>(), LocalDateTime.now());
            Post postFour  = postRepository.create(user, "body4", new ArrayList<>(), LocalDateTime.now());
            Post postFive  = postRepository.create(user, "body5", new ArrayList<>(), LocalDateTime.now());

            // Test full page
            List<Post> fullPage = postRepository.getByAuthorRolling(user, 5, null);
            assertEquals(5, fullPage.size());
            assertEquals(postFive, fullPage.get(0));

            // Test limit - no last
            List<Post> noLast = postRepository.getByAuthorRolling(user, 2, null);
            assertEquals(2, noLast.size());
            assertEquals(postFive, noLast.get(0));
            assertEquals(postFour, noLast.get(1));

            // Test limit - with last
            List<Post> withList = postRepository.getByAuthorRolling(user, 3, postFive.getId());
            assertEquals(3, withList.size());
            assertEquals(postFour, withList.get(0));
            assertEquals(postThree, withList.get(1));
            assertEquals(postTwo, withList.get(2));
        }
    }

    @Test
    void getTimeline()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPostRepository       postRepository       = new JpaPostRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            JpaCityRepository       cityRepository       = new JpaCityRepository(transaction);
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);

            City city   = cityRepository.get(1);
            User user   = TestingUtils.randomUser(userRepository, city);
            User friend = TestingUtils.randomUser(userRepository, city);
            friendshipRepository.accept(friendshipRepository.createRequest(user, friend));
            Post postOne   = postRepository.create(friend, "body1", new ArrayList<>(), LocalDateTime.now());
            Post postTwo   = postRepository.create(friend, "body2", new ArrayList<>(), LocalDateTime.now());
            Post postThree = postRepository.create(friend, "body3", new ArrayList<>(), LocalDateTime.now());

            // Test full page
            List<Post> fullPage = postRepository.getTimeline(user, 3, null);
            assertEquals(3, fullPage.size());
            assertEquals(postThree, fullPage.get(0));

            // Test limit - no last
            List<Post> noLast = postRepository.getTimeline(user, 2, null);
            assertEquals(2, noLast.size());
            assertEquals(postThree, noLast.get(0));
            assertEquals(postTwo, noLast.get(1));

            // Test limit - with last
            List<Post> withList = postRepository.getTimeline(user, 3, postThree.getId());
            assertEquals(2, withList.size());
            assertEquals(postTwo, withList.get(0));
            assertEquals(postOne, withList.get(1));
        }
    }
}