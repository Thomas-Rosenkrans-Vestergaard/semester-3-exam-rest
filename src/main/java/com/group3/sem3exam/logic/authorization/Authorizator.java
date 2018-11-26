package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.authentication.AuthenticationContext;

/**
 * Performs checks on an {@link AuthenticationContext} before performing some operation.
 * <p>
 * An example of the usage:
 * <code>
 * int userToRetrieve = 5;
 * new Authorizator(authenticationContext).check(new SensitiveDataCheck(userToRetrieve);
 * UserFacade userFacade = new UserFacade();
 * return userFacade.getSensitiveData(userToRetrieve);
 * });
 * </code>
 */
public class Authorizator
{

    /**
     * The authentication context to perform check upon.
     */
    private final AuthenticationContext authenticationContext;

    /**
     * Creates a new {@link Authorizator}.
     *
     * @param authenticationContext The authentication context to perform check upon.
     */
    public Authorizator(AuthenticationContext authenticationContext)
    {
        this.authenticationContext = authenticationContext;
    }

    /**
     * Checks that the provided {@link AuthenticationContext} passes all the provided checks.
     *
     * @param checks The checks the {@link AuthenticationContext} must pass.
     * @throws AuthorizationException When one or more of the check fails.
     */
    public void check(AuthorizationCheck... checks) throws AuthorizationException
    {
        for (AuthorizationCheck check : checks)
            check.check(this.authenticationContext);
    }
}
