package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.ReadRepository;

import java.util.List;

public interface PermissionTemplateRepository extends ReadRepository<PermissionTemplate, String>
{

    /**
     * Create a new service request template.
     *
     * @param message    The message to display to the user.
     * @param permissions The permissions the service is asking for.
     * @param service    The service requesting the permissions.
     * @return The newly creates {@link PermissionTemplate}.
     */
    PermissionTemplate create(String message, List<Permission> permissions, Service service);
}
