package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.authentication.AuthenticationContext;

import java.util.Arrays;
import java.util.List;

/**
 * An implementation of {@code AuthorizationCheck} that supports multiple other {@code AuthorizationCheck}s.
 */
public class MultipleAuthorizationCheck implements AuthorizationCheck
{

    /**
     * The contained checks.
     */
    private final List<AuthorizationCheck> authorizationChecks;

    /**
     * Creates a new {@link MultipleAuthorizationCheck}.
     *
     * @param authorizationChecks The checks to perform.
     */
    public MultipleAuthorizationCheck(List<AuthorizationCheck> authorizationChecks)
    {
        this.authorizationChecks = authorizationChecks;
    }

    /**
     * Performs the authorization check provided the authentication context.
     *
     * @param authenticationContext The authentication context to perform the check upon.
     */
    @Override
    public void check(AuthenticationContext authenticationContext)
    {
        for (AuthorizationCheck check : authorizationChecks)
            check.check(authenticationContext);
    }

    /**
     * Creates and returns a new {@link MultipleAuthorizationCheck}.
     *
     * @param checks The checks to perform.
     * @return The newly created instance.
     */
    public static MultipleAuthorizationCheck many(AuthorizationCheck... checks)
    {
        return new MultipleAuthorizationCheck(Arrays.asList(checks));
    }
}
