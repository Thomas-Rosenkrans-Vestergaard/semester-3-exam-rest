package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.services.Permission;
import com.group3.sem3exam.data.services.Service;

import java.util.HashSet;
import java.util.Set;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.*;

public class EagerAuthenticationContext implements AuthenticationContext
{

    /**
     * The type of the authenticated entity.
     */
    private AuthenticationType type;

    /**
     * The authenticated user, must not be null when the {@code type} of the {@link AuthenticationContext} is
     * {@code USER} or {@code SERVICE_REPRESENTING_USER}.
     */
    private User user;

    /**
     * The authenticated service, must not be null when the {@code type} of the {@link AuthenticationContext} is
     * {@code SERVICE} or {@code SERVICE_REPRESENTING_USER}.
     */
    private Service service;

    /**
     * The permissions granted by a user to a service. Only non-null when the type of the authentication
     * context is {@code SERVICE_REPRESENTING_USER}.
     */
    private Set<Permission> permissions;

    /**
     * Creates a new {@link AuthenticationContext}.
     *
     * @param type        The type of the authenticated entity.
     * @param user        The authenticated user, must not be null when the {@code type} of the {@link AuthenticationContext} is
     *                    {@code USER} or {@code SERVICE_REPRESENTING_USER}.
     * @param service     The authenticated service, must not be null when the {@code type} of the {@link AuthenticationContext} is
     *                    {@code SERVICE} or {@code SERVICE_REPRESENTING_USER}.
     * @param permissions The permissions granted by a user to a service. Only non-null when the type of the authentication
     *                    context is {@code SERVICE_REPRESENTING_USER}.
     */
    private EagerAuthenticationContext(AuthenticationType type, User user, Service service, Set<Permission> permissions)
    {
        if (type == null)
            throw new IllegalArgumentException("AuthenticationType must not be null.");

        if ((type == USER || type == SERVICE_REPRESENTING_USER) && user == null)
            throw new IllegalArgumentException("User must not be null.");
        if ((type == SERVICE || type == SERVICE_REPRESENTING_USER) && service == null)
            throw new IllegalArgumentException("Service must not be null.");
        if (type == SERVICE_REPRESENTING_USER && permissions == null)
            throw new IllegalArgumentException("Permissions must not be null.");

        this.type = type;
        this.user = user;
        this.service = service;
        this.permissions = permissions;
    }

    /**
     * Creates a new authentication context of the type {@code USER} using the provided user.
     *
     * @param user The authenticated user.
     * @return The newly created authentication context.
     */
    public static EagerAuthenticationContext user(User user)
    {
        return new EagerAuthenticationContext(USER, user, null, new HashSet<>());
    }

    /**
     * Creates a new authentication context of the type {@code SERVICE} using the provided service.
     *
     * @param service The authenticated service.
     * @return The newly created authentication context.
     */
    public static EagerAuthenticationContext service(Service service)
    {
        return new EagerAuthenticationContext(SERVICE, null, service, new HashSet<>());
    }

    /**
     * Returns the type of the authenticated entity.
     *
     * @return The type of the authenticated entity.
     */
    public AuthenticationType getType()
    {
        return this.type;
    }

    /**
     * Returns the id of the authenticated user, or the id of the represented user when {@code type} is
     * {@code SERVICE_REPRESENTING_USER}.
     *
     * @return The id of the authenticated user, or the id of the represented user when {@code type} is
     * {@code SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    @Override
    public Integer getUserId()
    {
        if (user == null)
            return null;

        return user.getId();
    }

    /**
     * Returns the authenticated user, or the represented user when {@code type} is
     * {@code SERVICE_REPRESENTING_USER}.
     *
     * @return The authenticated user, or the represented user when {@code type} is
     * {@code SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    @Override
    public User getUser()
    {
        return user;
    }

    @Override
    public Integer getServiceId()
    {
        if (service == null)
            return null;

        return service.getId();
    }

    @Override
    public Service getService()
    {
        return service;
    }

    @Override
    public Set<Permission> getServicePermissions()
    {
        return permissions;
    }
}