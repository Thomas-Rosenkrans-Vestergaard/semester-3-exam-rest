package com.group3.sem3exam.logic.authentication;

public enum AuthenticationType
{

    /**
     * The authentication context contains information about an authenticated user.
     */
    USER,

    /**
     * The authentication context contains information about a service. Services can
     * request permissions from users, thus allowing services to perform operations
     * on behalf of users.
     * <p>
     * When a service retains permissions from a user, an authentication type is {@code SERVICE_REPRESENTING_USER}.
     */
    SERVICE,

    /**
     * The authentication context contains information about a user that has given
     * permissions to a service. This allows the service to perform operations
     * on the data of the user, as if the user has performs the operation themself.
     * <p>
     * The given permissions are stored in the authentication context.
     */
    SERVICE_REPRESENTING_USER
}
