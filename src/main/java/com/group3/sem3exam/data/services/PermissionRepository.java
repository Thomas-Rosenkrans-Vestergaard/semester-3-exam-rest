package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.Repository;
import com.group3.sem3exam.data.services.entities.Permission;
import com.group3.sem3exam.data.services.entities.Service;

import java.time.LocalDateTime;
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

    /**
     * Updates the field keeping track of the time at which the permissions for the provided user
     * and service was last updated. If this is the first time the time has been updated for the provided
     * service and user combination, a new record of the provided time is inserted.
     *
     * @param service The service.
     * @param user    The user.
     * @param time    The time to set the 'permissions last updated' field to.
     */
    void setLastUpdated(Service service, User user, LocalDateTime time);

    /**
     * Returns the time when the permissions involving the provided service and user was last
     * updated.
     *
     * @param service The service.
     * @param user    The user.
     * @return The time when the permissions involving the provided service and user was last
     * updated. If the permissions have never been updated before, this method returns the current
     * system time. The timestamp is not persisted to the database.
     */
    LocalDateTime getLastUpdated(Service service, User user);
}
