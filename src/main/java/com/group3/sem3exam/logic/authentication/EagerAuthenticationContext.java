package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;

import java.util.Objects;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.SERVICE_REPRESENTING_USER;
import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;

public class EagerAuthenticationContext implements AuthenticationContext
{

    /**
     * The type of the authenticated entity.
     */
    private AuthenticationType type;

    /**
     * The authenticated user, must not be null when the {@code type} of the {@link AuthenticationContext} is
     * {@link AuthenticationType#USER} or {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     */
    private User user;

    /**
     * Creates a new {@link AuthenticationContext}.
     *
     * @param type The type of the authenticated entity.
     * @param user The authenticated user, must not be null when the {@code type} of the {@link AuthenticationContext} is
     *             * {@link AuthenticationType#USER} or {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     */
    private EagerAuthenticationContext(AuthenticationType type, User user)
    {
        if (type == null)
            throw new IllegalArgumentException("AuthenticationType must not be null.");

        if ((type == USER || type == SERVICE_REPRESENTING_USER) && user == null)
            throw new IllegalArgumentException("User must not be null.");

        this.type = type;
        this.user = user;
    }

    /**
     * Creates a new authentication context of the type {@link AuthenticationType#USER} using the provided user.
     *
     * @param user The authenticated user.
     * @return The newly created authentication context.
     */
    public static EagerAuthenticationContext user(User user)
    {
        return new EagerAuthenticationContext(USER, user);
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
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     *
     * @return The id of the authenticated user, or the id of the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    @Override
    public Integer getUserId()
    {
        return user.getId();
    }

    /**
     * Returns the authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}.
     *
     * @return The authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#SERVICE_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    @Override
    public User getUser()
    {
        return user;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AuthenticationContext)) return false;
        AuthenticationContext that = (AuthenticationContext) o;
        return getType() == that.getType() &&
               Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getType(), getUser());
    }
}
