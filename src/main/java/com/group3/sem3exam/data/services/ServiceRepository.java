package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.ReadRepository;
import com.group3.sem3exam.data.services.entities.Service;

public interface ServiceRepository extends ReadRepository<Service, Integer>
{

    /**
     * Creates a new service using the provided information.
     *
     * @param name         The name of the service.
     * @param passwordHash The hashed password of the service.
     * @return The newly created service.
     */
    Service create(String name, String passwordHash);

    /**
     * Returns the service with the provided name.
     *
     * @param name The name of the service to return.
     * @return The service with the provided name, {@code null} when no such record exists.
     */
    Service getByName(String name);

    /**
     * Disables the provided service
     *
     * @param service The service to disable.
     * @return The updated service entity.
     */
    Service disable(Service service);

    /**
     * Enables the provided service
     *
     * @param service The service to enable.
     * @return The updated service entity.
     */
    Service enable(Service service);
}
