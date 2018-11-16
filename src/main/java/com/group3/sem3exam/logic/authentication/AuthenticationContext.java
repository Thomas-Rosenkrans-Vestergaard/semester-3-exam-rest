package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;

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
     * {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}.
     *
     * @return The id of the authenticated user, or the id of the represented user when {@code type} is
     * {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    Integer getUserId();

    /**
     * Returns the authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}.
     *
     * @return The authenticated user, or the represented user when {@code type} is
     * {@link AuthenticationType#THIRD_PARTY_REPRESENTING_USER}. In all other cases this method returns
     * {@code null} to indicate that this authentication context does not contain user information.
     */
    User getUser();
}
