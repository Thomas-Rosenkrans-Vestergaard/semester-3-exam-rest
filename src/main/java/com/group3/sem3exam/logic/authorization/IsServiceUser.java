package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.authentication.AuthenticationContext;

public class IsServiceUser implements AuthorizationCheck
{

    /**
     * Checks that the provided authenticationContext represents a user using a service.
     *
     * @param authenticationContext The authentication context to perform the check upon.
     * @throws AuthorizationException
     */
    @Override
    public void check(AuthenticationContext authenticationContext) throws AuthorizationException
    {
        if (authenticationContext.getService() == null)
            throw new AuthorizationException("You must be a user using a service to access this endpoint.");
    }
}

