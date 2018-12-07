package com.group3.sem3exam.data.services;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCityRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepositoryTester;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.entities.PermissionRequest;
import com.group3.sem3exam.data.services.entities.PermissionTemplate;
import com.group3.sem3exam.data.services.entities.Service;
import com.group3.sem3exam.rest.JpaConnection;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeMap;

import static com.group3.sem3exam.data.services.entities.PermissionRequest.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class JpaPermissionRequestRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<PermissionRequest, String, JpaPermissionRequestRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaPermissionRequestRepository(JpaTestConnection.create()),
                        (repository) -> createPermissionRequestMap(repository),
                        "unknown"
                );

        return tester.getDynamicTests();
    }

    private TreeMap<String, PermissionRequest> createPermissionRequestMap(JpaPermissionRequestRepository repository)
    {
        JpaCityRepository                  cityRepository     = new JpaCityRepository(repository.getEntityManager());
        JpaUserRepository                  userRepository     = new JpaUserRepository(repository.getEntityManager());
        JpaServiceRepository               serviceRepository  = new JpaServiceRepository(repository.getEntityManager());
        JpaPermissionTemplateRepository    templateRepository = new JpaPermissionTemplateRepository(repository.getEntityManager());
        TreeMap<String, PermissionRequest> map                = new TreeMap<>();

        User               user     = TestingUtils.randomUser(userRepository, cityRepository.get(1));
        Service            service  = TestingUtils.randomService(serviceRepository);
        PermissionTemplate template = TestingUtils.randomPermissionTemplate(templateRepository, service);
        for (int i = 0; i < 5; i++) {
            PermissionRequest permissionRequest = repository.create(user, "callback", template);
            map.put(permissionRequest.getId(), permissionRequest);
        }

        return map;
    }

    @Test
    void create()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaCityRepository               cityRepository     = new JpaCityRepository(transaction);
            JpaUserRepository               userRepository     = new JpaUserRepository(transaction);
            JpaServiceRepository            serviceRepository  = new JpaServiceRepository(transaction);
            JpaPermissionTemplateRepository templateRepository = new JpaPermissionTemplateRepository(transaction);
            JpaPermissionRequestRepository  requestRepository  = new JpaPermissionRequestRepository(transaction);

            User               user              = TestingUtils.randomUser(userRepository, cityRepository.get(1));
            Service            service           = TestingUtils.randomService(serviceRepository);
            PermissionTemplate template          = TestingUtils.randomPermissionTemplate(templateRepository, service);
            PermissionRequest  permissionRequest = requestRepository.create(user, "callback", template);

            assertNotNull(permissionRequest.getId());
            assertEquals(user, permissionRequest.getUser());
            assertEquals(PENDING, permissionRequest.getStatus());
            assertEquals("callback", permissionRequest.getCallback());
            assertEquals(template, permissionRequest.getTemplate());
            assertTrue(LocalDateTime.now().minusMinutes(1).isBefore(permissionRequest.getCreatedAt()));
            assertTrue(permissionRequest.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(5)));
        }
    }

    @Test
    void updateStatus()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaCityRepository               cityRepository     = new JpaCityRepository(transaction);
            JpaUserRepository               userRepository     = new JpaUserRepository(transaction);
            JpaServiceRepository            serviceRepository  = new JpaServiceRepository(transaction);
            JpaPermissionTemplateRepository templateRepository = new JpaPermissionTemplateRepository(transaction);
            JpaPermissionRequestRepository  requestRepository  = new JpaPermissionRequestRepository(transaction);

            User               user              = TestingUtils.randomUser(userRepository, cityRepository.get(1));
            Service            service           = TestingUtils.randomService(serviceRepository);
            PermissionTemplate template          = TestingUtils.randomPermissionTemplate(templateRepository, service);
            PermissionRequest  permissionRequest = requestRepository.create(user, "callback", template);

            assertEquals(PENDING, permissionRequest.getStatus());
            requestRepository.updateStatus(permissionRequest, ACCEPTED);
            assertEquals(ACCEPTED, permissionRequest.getStatus());
            requestRepository.updateStatus(permissionRequest, REJECTED);
            assertEquals(REJECTED, permissionRequest.getStatus());
        }
    }
}