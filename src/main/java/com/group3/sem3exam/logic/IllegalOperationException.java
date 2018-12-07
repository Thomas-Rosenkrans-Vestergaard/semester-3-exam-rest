package com.group3.sem3exam.logic;

import com.group3.sem3exam.logic.authorization.AuthorizationException;

/**
 * Thrown when a some actor attempts to perform an operation, that cannot be performed due
 * to some specific state. Examples include attempting to send a friend request to someone
 * who is already a friend.
 * <p>
 * When the operation cannot be perform because the actor lacks permission, an
 * {@link AuthorizationException} should be thrown instead.
 */
public class IllegalOperationException extends FacadeException
{

    public IllegalOperationException(String errorMessage)
    {
        this(errorMessage, null);
    }

    public IllegalOperationException(String errorMessage, Throwable cause)
    {
        super("IllegalOperation", errorMessage, cause);
    }
}
