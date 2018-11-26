package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.services.Service;

import java.util.function.Supplier;

/**
 * Implementation of {@link AuthenticationContext} that lazily fetches Users.
 */
public class LazyAuthenticationContext implements AuthenticationContext
{

    /**
     * The authentication type of this context.
     */
    private final AuthenticationType authenticationType;

    /**
     * The user represented by this authentication context.
     */
    private final Integer userId;

    /**
     * The supplier that lazily fetches the first user instance. Note that the supplier
     * does not need to implement caching, this class returns that first result on subsequent
     * requests.
     */
    private Supplier<User> userSupplier;
    private User           lazyUserCache;

    /**
     * The service represented by this authentication context.
     */
    private final Integer serviceId;

    /**
     * The supplier that lazily fetches the first service instance. Note that the supplier
     * does not need to implement caching, this class returns that first result on subsequent
     * requests.
     */
    private Supplier<Service> serviceSupplier;
    private Service           lazyServiceCache;

    /**
     * Creates a new {@link LazyAuthenticationContext}.
     *
     * @param authenticationType The type of the {@link LazyAuthenticationContext}.
     * @param userId             The id of the user represented in the constructed authentication context.
     * @param userSupplier       The supplier that lazily fetches the first user instance. Note that the supplier
     *                           does not need to implement caching, this class returns that first result on subsequent
     *                           requests.
     * @param userId             The id of the service represented in the constructed authentication context.
     * @param userSupplier       The supplier that lazily fetches the first service instance. Note that the supplier
     *                           does not need to implement caching, this class returns that first result on subsequent
     *                           requests.
     * @return The newly constructed authentication context.
     */
    public LazyAuthenticationContext(
            AuthenticationType authenticationType,
            Integer userId,
            Supplier<User> userSupplier,
            Integer serviceId,
            Supplier<Service> serviceSupplier)
    {
        this.authenticationType = authenticationType;

        this.userId = userId;
        this.userSupplier = userSupplier;

        this.serviceId = serviceId;
        this.serviceSupplier = serviceSupplier;
    }

    /**
     * Constructs and returns a new {@link LazyAuthenticationContext} of the type {@link AuthenticationType#USER} with
     * the provided {@code userId} and {@code userSupplier}.
     *
     * @param userId       The id of the user represented in the constructed authentication context.
     * @param userSupplier The supplier that lazily fetches the first user instance. Note that the supplier
     *                     does not need to implement caching, this class returns that first result on subsequent
     *                     requests.
     * @return The newly constructed authentication context.
     */
    public static LazyAuthenticationContext user(Integer userId, Supplier<User> userSupplier)
    {
        return new LazyAuthenticationContext(AuthenticationType.USER, userId, userSupplier, null, () -> null);
    }

    /**
     * Creates a new {@link LazyAuthenticationContext} of the type {@link AuthenticationType#SERVICE}.
     *
     * @param service The service to create the authentication context for.
     * @return The authentication context.
     */
    public static LazyAuthenticationContext service(Service service)
    {
        return new LazyAuthenticationContext(AuthenticationType.SERVICE, null, () -> null, service.getId(), () -> service);
    }

    /**
     * Returns the type of the authenticated entity.
     *
     * @return The type of the authenticated entity.
     */
    @Override
    public AuthenticationType getType()
    {
        return authenticationType;
    }

    /**
     * Returns the id of the authenticated user, or the id of the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     *
     * @return The id of the authenticated user, or the id of the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    @Override
    public Integer getUserId()
    {
        return userId;
    }

    /**
     * Returns the authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     * <p>
     * Note that this method works lazily. The user is only retrieved from the supplier when this method is
     * first called. On subsequent calls the result of the first supplier invocation is returned.
     *
     * @return The authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    @Override
    public User getUser()
    {
        if (authenticationType == AuthenticationType.SERVICE)
            return null;

        if (lazyUserCache == null)
            lazyUserCache = userSupplier.get();

        return lazyUserCache;
    }

    /**
     * Returns the id of the authenticated service.
     *
     * @return The id of the authenticated service. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain service information.
     */
    @Override
    public Integer getServiceId()
    {
        return serviceId;
    }

    /**
     * Returns the authenticated service.
     * <p>
     * Note that this method works lazily. The user is only retrieved from the supplier when this method is
     * first called. On subsequent calls the result of the first supplier invocation is returned.
     *
     * @return The authenticated service. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain service information.
     */
    @Override
    public Service getService()
    {
        if (authenticationType == AuthenticationType.USER)
            return null;

        if (lazyServiceCache == null)
            lazyServiceCache = serviceSupplier.get();

        return lazyServiceCache;
    }
}
