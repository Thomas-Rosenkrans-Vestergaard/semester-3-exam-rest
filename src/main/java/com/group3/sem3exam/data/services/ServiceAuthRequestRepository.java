package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.ReadRepository;

public interface ServiceAuthRequestRepository extends ReadRepository<ServicePrivilegeRequest, Integer>
{

    /**
     * Creates a new service request.
     *
     * @param template The privileges to request from the user.
     * @param user     The user to request privileges from.
     * @return An entity representing the newly created service request.
     */
    ServicePrivilegeRequest request(ServiceAuthTemplate template, User user);

    /**
     * Marks the provided service request as {@link ServicePrivilegeRequest.Status#ACCEPTED}.
     *
     * @param request The request to mark as accepted.
     * @return The updated entity.
     */
    ServicePrivilegeRequest accept(ServicePrivilegeRequest request);

    /**
     * Marks the provided service request as {@link ServicePrivilegeRequest.Status#REJECTED}.
     *
     * @param request The request to mark as rejected.
     * @return The updated entity.
     */
    ServicePrivilegeRequest reject(ServicePrivilegeRequest request);
}
