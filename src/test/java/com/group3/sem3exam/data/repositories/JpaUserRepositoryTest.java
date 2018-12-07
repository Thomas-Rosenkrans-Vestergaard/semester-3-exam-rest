package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.ProfilePicture;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepositoryTester;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class JpaUserRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<User, Integer, JpaUserRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaUserRepository(JpaTestConnection.create()),
                        (repository) -> createUserMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, User> createUserMap(JpaUserRepository repository)
    {
        TreeMap<Integer, User> map  = new TreeMap<>();
        City                   city = new JpaCityRepository(repository.getEntityManager()).get(1);

        User user;

        user = repository.createUser("user1", "user1@email.com", "pass", city, Gender.MALE, LocalDate.now());
        map.put(user.getId(), user);

        user = repository.createUser("user2", "user2@email.com", "pass", city, Gender.FEMALE, LocalDate.now());
        map.put(user.getId(), user);

        user = repository.createUser("user3", "user3@email.com", "pass", city, Gender.MALE, LocalDate.now());
        map.put(user.getId(), user);

        user = repository.createUser("user4", "user4@email.com", "pass", city, Gender.MALE, LocalDate.now());
        map.put(user.getId(), user);

        user = repository.createUser("user5", "user5@email.com", "pass", city, Gender.FEMALE, LocalDate.now());
        map.put(user.getId(), user);

        return map;
    }

    @Test
    void getByEmail()
    {
        try (JpaUserRepository tur = new JpaUserRepository(JpaTestConnection.create())) {
            tur.begin();
            City city = new JpaCityRepository(tur.getEntityManager()).get(1);
            assertNull(tur.getByEmail("does_not_exist"));

            assertNull(tur.getByEmail("some@email.com"));
            User user = tur.createUser("email-user", "some@email.com", "pass", city, Gender.FEMALE, LocalDate.now());
            assertEquals(user, tur.getByEmail("some@email.com"));
        }
    }

    @Test
    void updateProfilePicture()
    {
        try (JpaUserRepository tur = new JpaUserRepository(JpaTestConnection.create())) {
            tur.begin();
            City city = new JpaCityRepository(tur.getEntityManager()).get(1);
            User user = TestingUtils.randomUser(tur, city);
            assertNull(user.getProfilePicture());
            tur.updateProfilePicture(user, "full", "thumbnail");
            ProfilePicture profilePicture = user.getProfilePicture();
            assertNotNull(profilePicture);
            assertEquals("full", profilePicture.getFull());
            assertEquals(user, profilePicture.getUser());
            assertEquals("thumbnail", profilePicture.getThumbnail());
        }
    }

    @Test
    void searchUsers()
    {
        try (JpaUserRepository tur = new JpaUserRepository(JpaTestConnection.create())) {
            tur.begin();
            City city = new JpaCityRepository(tur.getEntityManager()).get(1);
            User user = TestingUtils.randomUser(tur, city);
            assertEquals(1, tur.searchUsers(user.getName().substring(0, 1)).size());
            assertEquals(0, tur.searchUsers(user.getName().substring(1)).size());
        }
    }
}
