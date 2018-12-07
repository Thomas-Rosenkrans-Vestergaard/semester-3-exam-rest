package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.services.entities.Permission;
import com.group3.sem3exam.data.services.entities.Service;

import java.util.Set;

public interface AuthenticationContext
{

    /**
     * Returns the type of the authenticated entity.
     *
     * @return The type of the authenticated entity.
     */
    AuthenticationType getType();

    /**
     * Returns the id of the authenticated user, or the id of the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     *
     * @return The id of the authenticated user, or the id of the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    Integer getUserId();

    /**
     * Returns the authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     *
     * @return The authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    User getUser();

    /**
     * Returns the id of the authenticated service.
     *
     * @return The authenticated service. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain service information.
     */
    Integer getServiceId();

    /**
     * Returns the authenticated service.
     *
     * @return The authenticated service. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain service information.
     */
    Service getService();

    /**
     * Returns the permissions granted by a user to a service.
     * @return The permissions granted by a user to a service. Only non-null when the type of the authentication
     * context is {@code SERVICE_REPRESENTING_USER}.
     */
    Set<Permission> getServicePermissions();
}
