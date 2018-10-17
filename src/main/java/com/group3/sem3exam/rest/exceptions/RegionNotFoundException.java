package com.group3.sem3exam.rest.exceptions;

public class RegionNotFoundException extends APIException
{

    public RegionNotFoundException(Integer id)
    {
        super("RegionNotFoundError", String.format("The region with the provided id %d could not be found.", id), 404, null);
    }
}
