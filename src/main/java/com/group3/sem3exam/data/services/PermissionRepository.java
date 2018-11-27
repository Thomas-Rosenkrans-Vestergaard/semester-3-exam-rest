package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;

import java.util.List;

public interface PermissionRepository
{

    /**
     * Returns the permissions provided to the provided service by the provided user.
     *
     * @param user    The user.
     * @param service The service.
     * @return The permissions provided to the provided service by the provided user.
     */
    List<Permission> getPermissionsFor(Service service, User user);
}
