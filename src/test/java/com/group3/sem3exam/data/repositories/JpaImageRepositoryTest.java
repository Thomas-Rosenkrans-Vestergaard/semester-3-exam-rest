package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaImageRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<Image, Integer, JpaImageRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaImageRepository(JpaTestConnection.create()),
                        (repository) -> createImageMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, Image> createImageMap(JpaImageRepository imageRepository)
    {
        TreeMap<Integer, Image> map = new TreeMap<>();

        JpaUserRepository userRepository = new JpaUserRepository(imageRepository.getEntityManager());

        City city = new JpaCityRepository(imageRepository.getEntityManager()).get(1);
        User user = TestingUtils.randomUser(userRepository, city);

        for (int i = 0; i < 5; i++) {
            Image image = imageRepository.create("description" + i, "full" + i, "thumbnail" + i, user);
            map.put(image.getId(), image);
        }

        return map;
    }

    @Test
    void create()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaUserRepository  userRepository  = new JpaUserRepository(transaction);
            JpaCityRepository  cityRepository  = new JpaCityRepository(transaction);
            JpaImageRepository imageRepository = new JpaImageRepository(transaction);

            User user = TestingUtils.randomUser(userRepository, cityRepository.get(1));

            Image image = imageRepository.create("description", "full", "thumbnail", user);

            assertEquals("description", image.getDescription());
            assertEquals("full", image.getFull());
            assertEquals("thumbnail", image.getThumbnail());
            assertEquals(user, image.getUser());
        }
    }

    @Test
    void getByUser()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaUserRepository  userRepository  = new JpaUserRepository(transaction);
            JpaCityRepository  cityRepository  = new JpaCityRepository(transaction);
            JpaImageRepository imageRepository = new JpaImageRepository(transaction);

            User user = TestingUtils.randomUser(userRepository, cityRepository.get(1));

            Image imageOne = imageRepository.create("description1", "full1", "thumbnail1", user);
            Image imageTwo = imageRepository.create("description2", "full2", "thumbnail2", user);

            List<Image> results = imageRepository.getByUser(user);
            assertEquals(2, results.size());
            assertEquals(imageOne, results.get(0));
            assertEquals(imageTwo, results.get(1));
        }
    }

    @Test
    void getByUserPaginated()
    {

        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaUserRepository  userRepository  = new JpaUserRepository(transaction);
            JpaCityRepository  cityRepository  = new JpaCityRepository(transaction);
            JpaImageRepository imageRepository = new JpaImageRepository(transaction);

            User user = TestingUtils.randomUser(userRepository, cityRepository.get(1));

            Image imageOne   = imageRepository.create("description1", "full1", "thumbnail1", user);
            Image imageTwo   = imageRepository.create("description2", "full2", "thumbnail2", user);
            Image imageThree = imageRepository.create("description3", "full3", "thumbnail3", user);

            List<Image> results;

            // PageSize
            results = imageRepository.getByUserPaginated(user, 2, 0);
            assertEquals(2, results.size());
            assertEquals(imageOne, results.get(0));

            // PageNumber
            results = imageRepository.getByUserPaginated(user, 2, 2);
            assertEquals(1, results.size());
            assertEquals(imageThree, results.get(0));
        }
    }

    @Test
    void countByUser()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaUserRepository  userRepository  = new JpaUserRepository(transaction);
            JpaCityRepository  cityRepository  = new JpaCityRepository(transaction);
            JpaImageRepository imageRepository = new JpaImageRepository(transaction);

            User user = TestingUtils.randomUser(userRepository, cityRepository.get(1));

            assertEquals(0, imageRepository.countByUser(user));
            imageRepository.create("description1", "full1", "thumbnail1", user);
            assertEquals(1, imageRepository.countByUser(user));
            imageRepository.create("description2", "full2", "thumbnail2", user);
            assertEquals(2, imageRepository.countByUser(user));
        }
    }
}