package com.group3.sem3exam.rest.exceptions;

public class ImageNotFoundException extends APIException
{

    public ImageNotFoundException(Integer id)
    {
        super("ImageNotFoundError", String.format("The Image with the provided id %d could not be found.", id), 404, null);
    }
}
