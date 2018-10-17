package com.group3.sem3exam.rest.exceptions;

public class CityNotFoundException extends APIException
{

    public CityNotFoundException(Integer id)
    {
        super("CityNotFoundError", String.format("The city with the provided id %d could not be found.", id), 404, null);
    }
}
