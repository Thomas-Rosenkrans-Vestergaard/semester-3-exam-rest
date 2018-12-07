package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.authentication.AuthenticationContext;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;

public class IsUser implements AuthorizationCheck
{

    /**
     * Checks that the provided authenticationContext represents a user.
     * <p>
     * The authentication context must not represent a service user.
     *
     * @param authenticationContext The authentication context to perform the check upon.
     * @throws AuthorizationException
     */
    @Override
    public void check(AuthenticationContext authenticationContext) throws AuthorizationException
    {
        if (authenticationContext.getType() != USER)
            throw new AuthorizationException("You must be a user to access this endpoint.");
    }
}