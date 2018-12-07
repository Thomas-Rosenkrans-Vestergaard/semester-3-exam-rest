package com.group3.sem3exam.data.services;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepositoryTester;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.entities.Permission;
import com.group3.sem3exam.data.services.entities.PermissionTemplate;
import com.group3.sem3exam.data.services.entities.Service;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaPermissionTemplateRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<PermissionTemplate, String, JpaPermissionTemplateRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaPermissionTemplateRepository(JpaTestConnection.create()),
                        (repository) -> createPermissionTemplateMap(repository),
                        "unknown"
                );

        return tester.getDynamicTests();
    }

    private TreeMap<String, PermissionTemplate> createPermissionTemplateMap(JpaPermissionTemplateRepository repository)
    {
        JpaServiceRepository                serviceRepository = new JpaServiceRepository(repository.getEntityManager());
        TreeMap<String, PermissionTemplate> map               = new TreeMap<>();

        Service service = TestingUtils.randomService(serviceRepository);
        for (int i = 0; i < 5; i++) {
            PermissionTemplate permissionTemplate = repository.create("name" + i, "message" + i, new ArrayList<>(), service);
            map.put(permissionTemplate.getId(), permissionTemplate);
        }

        return map;
    }

    @Test
    void create()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaServiceRepository            serviceRepository  = new JpaServiceRepository(transaction);
            JpaPermissionTemplateRepository templateRepository = new JpaPermissionTemplateRepository(transaction);

            Service            service          = TestingUtils.randomService(serviceRepository);
            List<Permission>   inputPermissions = Arrays.asList(Permission.CREATE_POST, Permission.CREATE_POST);
            PermissionTemplate template         = templateRepository.create("name", "message", inputPermissions, service);

            assertNotNull(template.getId());
            assertEquals("name", template.getName());
            assertEquals("message", template.getMessage());
            assertEquals(inputPermissions, template.getPermissions());
            assertEquals(service, template.getService());
        }
    }

    @Test
    void getByService()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaServiceRepository            serviceRepository  = new JpaServiceRepository(transaction);
            JpaPermissionTemplateRepository templateRepository = new JpaPermissionTemplateRepository(transaction);

            Service service = TestingUtils.randomService(serviceRepository);

            assertTrue(templateRepository.getByService(null).isEmpty());
            assertTrue(templateRepository.getByService(service).isEmpty());

            List<PermissionTemplate> templates = new ArrayList<>();
            templates.add(templateRepository.create("name1", "message1", new ArrayList<>(), service));
            templates.add(templateRepository.create("name2", "message2", new ArrayList<>(), service));
            templates.add(templateRepository.create("name3", "message3", new ArrayList<>(), service));

            assertEquals(templates, templateRepository.getByService(service));
        }
    }

    @Test
    void getByName()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaServiceRepository            serviceRepository  = new JpaServiceRepository(transaction);
            JpaPermissionTemplateRepository templateRepository = new JpaPermissionTemplateRepository(transaction);

            Service service = TestingUtils.randomService(serviceRepository);

            assertTrue(templateRepository.getByService(null).isEmpty());
            assertTrue(templateRepository.getByService(service).isEmpty());

            PermissionTemplate template = templateRepository.create("name", "message", new ArrayList<>(), service);

            assertNull(templateRepository.getByName(null, ""));
            assertNull(templateRepository.getByName(service, ""));
            assertEquals(template, templateRepository.getByName(service, "name")); // Matches
        }
    }
}