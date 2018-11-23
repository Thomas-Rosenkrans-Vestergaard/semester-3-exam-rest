package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.*;

import java.util.List;

public interface ServiceRequestRepository extends ReadRepository<ServiceRequest, Integer>
{

    /**
     * Declares a new service request template.
     *
     * @param message    The message to display to the user.
     * @param privileges The privileges the service is asking for.
     * @param service    The service requesting the privileges.
     * @return The newly creates {@link ServiceRequestTemplate}.
     */
    ServiceRequestTemplate declare(String message, List<ServicePrivilege> privileges, Service service);

    /**
     * Returns the service request template with the provided id.
     *
     * @param id The id of the service request template to return.
     * @return The service request template with the provided id, {@code null} when no such record exists.
     */
    ServiceRequestTemplate getDeclaration(Integer id);

    /**
     * Creates a new service request.
     *
     * @param template The privileges to request from the user.
     * @param user     The user to request privileges from.
     * @return An entity representing the newly created service request.
     */
    ServiceRequest request(ServiceRequestTemplate template, User user);

    /**
     * Marks the provided service request as {@link com.group3.sem3exam.data.entities.ServiceRequest.Status#ACCEPTED}.
     *
     * @param request The request to mark as accepted.
     * @return The updated entity.
     */
    ServiceRequest accept(ServiceRequest request);

    /**
     * Marks the provided service request as {@link com.group3.sem3exam.data.entities.ServiceRequest.Status#REJECTED}.
     *
     * @param request The request to mark as rejected.
     * @return The updated entity.
     */
    ServiceRequest reject(ServiceRequest request);
}
