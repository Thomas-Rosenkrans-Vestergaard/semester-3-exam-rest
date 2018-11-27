package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.ReadRepository;

public interface PermissionRequestRepository extends ReadRepository<PermissionRequest, String>
{

    /**
     * Requests permissions from the provided user for the provided service owning the provided template,
     * with the permissions defined in the provided template.
     *
     * @param user     The user that is being asked for permission from the service.
     * @param template The template containing the permissions granted to the user, if the request is accepted.
     * @return The newly create permission request.
     */
    PermissionRequest create(User user, PermissionTemplate template);

    /**
     * Marks the provided service request as {@link PermissionRequest.Status#ACCEPTED}.
     *
     * @param request The request to mark as accepted.
     * @return The updated entity.
     */
    PermissionRequest accept(PermissionRequest request);

    /**
     * Marks the provided service request as {@link PermissionRequest.Status#REJECTED}.
     *
     * @param request The request to mark as rejected.
     * @return The updated entity.
     */
    PermissionRequest reject(PermissionRequest request);
}
