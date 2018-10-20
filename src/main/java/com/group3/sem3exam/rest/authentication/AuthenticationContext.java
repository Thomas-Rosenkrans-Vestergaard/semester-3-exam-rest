package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.data.entities.User;

import java.util.Objects;

import static com.group3.sem3exam.rest.authentication.AuthenticationType.THIRD_PARTY_REPRESENTING_USER;
import static com.group3.sem3exam.rest.authentication.AuthenticationType.USER;

public class AuthenticationContext
{

    /**
     * The type of the authenticated entity.
     */
    private AuthenticationType type;

    /**
     * The authenticated user, must not be null when the {@code type} of the {@link AuthenticationContext} is
     * {@link AuthenticationType#USER} or {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}.
     */
    private User user;

    /**
     * Creates a new {@link AuthenticationContext}.
     *
     * @param type The type of the authenticated entity.
     * @param user The authenticated user, must not be null when the {@code type} of the {@link AuthenticationContext} is
     *             * {@link AuthenticationType#USER} or {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}.
     */
    private AuthenticationContext(AuthenticationType type, User user)
    {
        if (type == null)
            throw new IllegalArgumentException("AuthenticationType must not be null.");

        if ((type == USER || type == THIRD_PARTY_REPRESENTING_USER) && user == null)
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
    public static AuthenticationContext user(User user)
    {
        return new AuthenticationContext(USER, user);
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
     * Returns the authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}.
     *
     * @return The authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}.
     */
    public User getUser()
    {
        return this.user;
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
