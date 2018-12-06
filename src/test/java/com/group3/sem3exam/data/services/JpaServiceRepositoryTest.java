package com.group3.sem3exam.data.services;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepositoryTester;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.entities.Service;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.TreeMap;

import static com.group3.sem3exam.data.services.entities.Service.Status.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaServiceRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<Service, Integer, JpaServiceRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaServiceRepository(JpaTestConnection.create()),
                        (repository) -> createServiceMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, Service> createServiceMap(JpaServiceRepository repository)
    {
        TreeMap<Integer, Service> map = new TreeMap<>();

        for (int i = 0; i < 5; i++) {
            Service service = repository.create("service" + i, "password" + i);
            map.put(service.getId(), service);
        }

        return map;
    }

    @Test
    void create()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaServiceRepository serviceRepository = new JpaServiceRepository(transaction);

            Service service = serviceRepository.create("Service Name", "Service Password");

            assertNotNull(service.getId());
            assertEquals("Service Name", service.getName());
            assertEquals("Service Password", service.getPasswordHash());
            assertEquals(PENDING, service.getStatus());
        }
    }

    @Test
    void getByName()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaServiceRepository serviceRepository = new JpaServiceRepository(transaction);

            assertNull(serviceRepository.getByName("Service Name"));
            Service service = serviceRepository.create("Service Name", "Service Password");
            assertEquals(service, serviceRepository.getByName("Service Name"));
        }
    }

    @Test
    void disable()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaServiceRepository serviceRepository = new JpaServiceRepository(transaction);

            Service service = serviceRepository.create("Service Name", "Service Password");
            assertEquals(PENDING, service.getStatus());
            service = serviceRepository.disable(service);
            assertEquals(DISABLED, service.getStatus());
        }
    }

    @Test
    void enable()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaServiceRepository serviceRepository = new JpaServiceRepository(transaction);

            Service service = serviceRepository.create("Service Name", "Service Password");
            assertEquals(PENDING, service.getStatus());
            service = serviceRepository.enable(service);
            assertEquals(ENABLED, service.getStatus());
        }
    }
}