package com.group3.sem3exam.data.services;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepositoryTester;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.entities.AuthRequest;
import com.group3.sem3exam.data.services.entities.PermissionTemplate;
import com.group3.sem3exam.data.services.entities.Service;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeMap;

import static com.group3.sem3exam.data.services.entities.AuthRequest.Status.COMPLETED;
import static com.group3.sem3exam.data.services.entities.AuthRequest.Status.READY;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

class JpaAuthRequestRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<AuthRequest, String, JpaAuthRequestRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaAuthRequestRepository(JpaTestConnection.create()),
                        (repository) -> createAuthRequestMap(repository),
                        "unknown"
                );

        return tester.getDynamicTests();
    }

    private TreeMap<String, AuthRequest> createAuthRequestMap(JpaAuthRequestRepository requestRepository)
    {
        EntityManager                   entityManager      = requestRepository.getEntityManager();
        JpaServiceRepository            serviceRepository  = new JpaServiceRepository(entityManager);
        JpaPermissionTemplateRepository templateRepository = new JpaPermissionTemplateRepository(entityManager);

        TreeMap<String, AuthRequest> map = new TreeMap<>(String::compareTo);

        Service            service  = TestingUtils.randomService(serviceRepository);
        PermissionTemplate template = TestingUtils.randomPermissionTemplate(templateRepository, service);
        for (int i = 0; i < 5; i++) {
            AuthRequest authRequest = requestRepository.create(service, "callback" + i, template);
            map.put(authRequest.getId(), authRequest);
        }

        return map;
    }

    @Test
    void create()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaAuthRequestRepository        requestRepository  = new JpaAuthRequestRepository(transaction);
            JpaServiceRepository            serviceRepository  = new JpaServiceRepository(transaction);
            JpaPermissionTemplateRepository templateRepository = new JpaPermissionTemplateRepository(transaction);

            Service     service = TestingUtils.randomService(serviceRepository);
            AuthRequest authRequest;

            // With permission template
            PermissionTemplate template = TestingUtils.randomPermissionTemplate(templateRepository, service);
            authRequest = requestRepository.create(service, "callback", template);
            assertNotNull(authRequest.getId());
            assertEquals("callback", authRequest.getCallback());
            assertEquals(template, authRequest.getTemplate());
            assertNotNull(authRequest.getCreatedAt());
            assertTrue(LocalDateTime.now().minusMinutes(1).isBefore(authRequest.getCreatedAt()));
            assertEquals(READY, authRequest.getStatus());

            // Without permission template
            authRequest = requestRepository.create(service, "callback", null);
            assertNotNull(authRequest.getId());
            assertEquals("callback", authRequest.getCallback());
            assertNull(authRequest.getTemplate());
            assertTrue(LocalDateTime.now().minusMinutes(1).isBefore(authRequest.getCreatedAt()));
            assertEquals(READY, authRequest.getStatus());
        }
    }

    @Test
    void completed()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaAuthRequestRepository requestRepository = new JpaAuthRequestRepository(transaction);
            JpaServiceRepository     serviceRepository = new JpaServiceRepository(transaction);
            Service                  service           = TestingUtils.randomService(serviceRepository);
            AuthRequest              authRequest       = requestRepository.create(service, "callback", null);

            assertEquals(READY, authRequest.getStatus());
            requestRepository.completed(authRequest);
            assertEquals(COMPLETED, authRequest.getStatus());
        }
    }
}