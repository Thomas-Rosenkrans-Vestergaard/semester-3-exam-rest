package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.Repository;

import java.util.List;
import java.util.Set;

public interface PermissionRepository extends Repository
{

    /**
     * Returns the permissions provided to the provided service by the provided user.
     *
     * @param user    The user.
     * @param service The service.
     * @return The permissions provided to the provided service by the provided user.
     */
    Set<Permission> getPermissionsFor(Service service, User user);
}
