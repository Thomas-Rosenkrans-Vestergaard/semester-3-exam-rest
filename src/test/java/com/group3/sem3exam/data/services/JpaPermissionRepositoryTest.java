package com.group3.sem3exam.data.services;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.JpaCityRepository;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.entities.Permission;
import com.group3.sem3exam.data.services.entities.PermissionRequest;
import com.group3.sem3exam.data.services.entities.PermissionTemplate;
import com.group3.sem3exam.data.services.entities.Service;
import com.group3.sem3exam.rest.JpaConnection;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.group3.sem3exam.data.services.entities.Permission.CREATE_POST;
import static com.group3.sem3exam.data.services.entities.Permission.DELETE_POST;
import static com.group3.sem3exam.data.services.entities.PermissionRequest.Status.ACCEPTED;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JpaPermissionRepositoryTest
{

    @Test
    void getPermissionsForAccepted()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaCityRepository               cityRepository       = new JpaCityRepository(transaction);
            JpaUserRepository               userRepository       = new JpaUserRepository(transaction);
            JpaServiceRepository            serviceRepository    = new JpaServiceRepository(transaction);
            JpaPermissionTemplateRepository templateRepository   = new JpaPermissionTemplateRepository(transaction);
            JpaPermissionRequestRepository  requestRepository    = new JpaPermissionRequestRepository(transaction);
            JpaPermissionRepository         permissionRepository = new JpaPermissionRepository(transaction);

            List<Permission>   inputPermissions  = Arrays.asList(CREATE_POST, DELETE_POST);
            User               user              = TestingUtils.randomUser(userRepository, cityRepository.get(1));
            Service            service           = TestingUtils.randomService(serviceRepository);
            PermissionTemplate template          = templateRepository.create("n", "m", inputPermissions, service);
            PermissionRequest  permissionRequest = requestRepository.create(user, "callback", template);

            assertEquals(0, permissionRepository.getPermissionsFor(service, user).size());
            requestRepository.updateStatus(permissionRequest, ACCEPTED);
            Set<Permission> result = permissionRepository.getPermissionsFor(service, user);

            assertEquals(inputPermissions.size(), result.size());
            assertTrue(result.containsAll(inputPermissions));
        }
    }

    @Test
    void setAndSetLastUpdated()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaPermissionRepository permissionRepository = new JpaPermissionRepository(transaction);
            JpaCityRepository       cityRepository       = new JpaCityRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            JpaServiceRepository    serviceRepository    = new JpaServiceRepository(transaction);

            User    user    = TestingUtils.randomUser(userRepository, cityRepository.get(1));
            Service service = TestingUtils.randomService(serviceRepository);

            LocalDateTime get = permissionRepository.getLastUpdated(service, user);
            assertNotNull(get); // Check that a new last-updated was created
            assertTrue(get.isBefore(LocalDateTime.now().plusSeconds(5)));
            assertTrue(get.isAfter(LocalDateTime.now().minusSeconds(5)));

            LocalDateTime set = LocalDateTime.now().minusDays(1);
            permissionRepository.setLastUpdated(service, user, set);

            assertEquals(set, permissionRepository.getLastUpdated(service, user));
        }
    }
}