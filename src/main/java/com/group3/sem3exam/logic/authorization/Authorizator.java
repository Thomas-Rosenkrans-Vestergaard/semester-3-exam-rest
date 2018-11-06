package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.logic.authentication.AuthenticationContext;

import java.util.concurrent.Callable;

/**
 * Performs checks on an {@link AuthenticationContext} before performing some operation.
 * <p>
 * An example of the usage:
 * <code>
 *  int userToRetrieve = 5;
 *  return new Authorizator(authenticationContext).attempt(new SensitiveDataCheck(userToRetrieve), () -> {
 *      UserFacade userFacade = new UserFacade();
 *      return userFacade.getSensitiveData(userToRetrieve);
 *  });
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
     * Performs the provided {@code operation} when the provided check passes. The operation cannot return a value.
     *
     * @param check     The check that must check for the operation to be performed.
     * @param operation The operation to perform when the provided {@code check} passes.
     * @throws Exception Any exception thrown from the operation or the provided {@code check}.
     * @see Authorizator#attempt(AuthorizationCheck, Callable)
     */
    public void attempt(AuthorizationCheck check, ExceptionRunnable operation) throws Exception
    {
        check.check(this.authenticationContext);

        operation.run();
    }

    /**
     * Performs the provided {@code operation} when the provided check passes. The method returns the value returned by
     * the provided {@code operation}.
     *
     * @param check     The check that must check for the operation to be performed.
     * @param operation The operation to perform when the provided {@code check} passes.
     * @return The value returned by the {@code operation}.
     * @throws Exception Any exception thrown from the operation or the provided {@code check}.
     * @see Authorizator#attempt(AuthorizationCheck, ExceptionRunnable)
     */
    public <T> T attempt(AuthorizationCheck check, Callable<T> operation) throws Exception
    {
        check.check(authenticationContext);

        return operation.call();
    }

    @FunctionalInterface
    public interface ExceptionRunnable
    {

        public void run() throws Exception;
    }
}
