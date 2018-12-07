package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.CrudRepository;
import com.group3.sem3exam.data.services.entities.PermissionRequest;
import com.group3.sem3exam.data.services.entities.PermissionTemplate;

public interface PermissionRequestRepository extends CrudRepository<PermissionRequest, String>
{

    /**
     * Requests permissions from the provided user for the provided service owning the provided template,
     * with the permissions defined in the provided template.
     *
     * @param user     The user that is being asked for permission from the service.
     * @param callback The callback to call when the user updates the permissions of the request.
     * @param template The template containing the permissions granted to the user, if the request is accepted.
     * @return The newly create permission request.
     */
    PermissionRequest create(User user, String callback, PermissionTemplate template);

    /**
     * Updates the status of the permission request.
     *
     * @param request The request to mark as accepted.
     * @param status  The new status of the permission request.
     * @return The updated entity.
     */
    PermissionRequest updateStatus(PermissionRequest request, PermissionRequest.Status status);
}
