package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.ReadRepository;

import java.util.List;

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

    /**
     * Searches for the provided name in the repository, returning a paginated view of the results.
     *
     * @param name       The name to search for in the repository.
     * @param pageSize   The number of elements in the view to return. Where {@code pageSize >= 0}.
     * @param pageNumber The number of the page to return, starts at 1. Where {@code pageNumber > 0}.
     * @return The paginated view of the results of the search.
     */
    List<Service> searchPaginated(String name, int pageSize, int pageNumber);
}
