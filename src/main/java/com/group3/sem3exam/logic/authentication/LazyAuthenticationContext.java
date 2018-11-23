package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;

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
     * Creates a new {@link LazyAuthenticationContext}.
     *
     * @param authenticationType The type of the {@link LazyAuthenticationContext}.
     * @param userId             The id of the user represented in the constructed authentication context.
     * @param userSupplier       The supplier that lazily fetches the first user instance. Note that the supplier
     *                           does not need to implement caching, this class returns that first result on subsequent
     *                           requests.
     * @return The newly constructed authentication context.
     */
    public LazyAuthenticationContext(AuthenticationType authenticationType, Integer userId, Supplier<User> userSupplier)
    {
        this.authenticationType = authenticationType;
        this.userId = userId;
        this.userSupplier = userSupplier;
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
        return new LazyAuthenticationContext(AuthenticationType.USER, userId, userSupplier);
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
}
