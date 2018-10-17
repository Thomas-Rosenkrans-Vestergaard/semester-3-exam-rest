package com.group3.sem3exam.rest.exceptions;

public class UserNotFoundException extends APIException
{

    public UserNotFoundException(Integer id)
    {
        super("UserNotFoundError", String.format("The user with the provided id %d could not be found.", id), 404, null);
    }
}
