package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.ReadRepository;

import java.util.List;

public interface PermissionTemplateRepository extends ReadRepository<PermissionTemplate, String>
{

    /**
     * Create a new service request template.
     *
     * @param name        The internal name of the template.
     * @param message     The message to display to the user.
     * @param permissions The permissions the service is asking for.
     * @param service     The service requesting the permissions.
     * @return The newly creates {@link PermissionTemplate}.
     */
    PermissionTemplate create(String name, String message, List<Permission> permissions, Service service);

    /**
     * Returns the templates owned by the provided service.
     *
     * @param service The service to return the templates of.
     * @return The templates owned by the provided service.
     */
    List<PermissionTemplate> getByService(Service service);

    /**
     * Returns the template with the provided name that is owned by the provided service.
     *
     * @param service The service that owns the permission template to return.
     * @param name    The name of the permission template to return.
     * @return The template with the provided name that is owned by the provided service, {@code null} when no
     * such record exists.
     */
    PermissionTemplate getByName(Service service, String name);
}
