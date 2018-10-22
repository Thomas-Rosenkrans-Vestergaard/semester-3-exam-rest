package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TransactionalUserRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        TransactionalCrudRepositoryTester<User, Integer, TransactionalUserRepository> tester =
                new TransactionalCrudRepositoryTester<User, Integer, TransactionalUserRepository>(
                        () -> new TransactionalUserRepository(JpaTestConnection.create()),
                        (repository) -> createUserMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, User> createUserMap(TransactionalUserRepository repository)
    {
        TreeMap<Integer, User> map  = new TreeMap<>();
        City                   city = new TransactionalCityRepository(repository.getEntityManager()).get(1);

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
        try (TransactionalUserRepository tur = new TransactionalUserRepository(JpaTestConnection.create())) {
            tur.begin();
            City city = new TransactionalCityRepository(tur.getEntityManager()).get(1);
            assertNull(tur.getByEmail("does_not_exist"));

            assertNull(tur.getByEmail("some@email.com"));
            User user = tur.createUser("email-user", "some@email.com", "pass", city, Gender.FEMALE, LocalDate.now());
            assertEquals(user, tur.getByEmail("some@email.com"));
        }
    }
}
