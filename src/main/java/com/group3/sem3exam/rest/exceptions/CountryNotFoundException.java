package com.group3.sem3exam.rest.exceptions;

public class CountryNotFoundException extends APIException
{


    public CountryNotFoundException(Integer id)
    {
        super("CountryNotFoundError", String.format("The user with the provided id %d could not be found.", id), 404, null);
    }
}
