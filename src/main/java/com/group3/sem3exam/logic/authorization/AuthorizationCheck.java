package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.authentication.AuthenticationContext;

public interface AuthorizationCheck
{

    /**
     * Performs the authorization check provided the authentication context.
     *
     * @param authenticationContext The authentication context to perform the check upon.
     * @throws AuthorizationException When the check fails.
     */
    void check(AuthenticationContext authenticationContext) throws AuthorizationException;
}
