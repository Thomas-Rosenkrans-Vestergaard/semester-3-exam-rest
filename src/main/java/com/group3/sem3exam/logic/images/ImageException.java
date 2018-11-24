package com.group3.sem3exam.logic.images;

import com.group3.sem3exam.logic.FacadeException;

public class ImageException extends FacadeException
{

    public ImageException(String errorMessage)
    {
        this(errorMessage, null);
    }

    public ImageException(String errorMessage, Throwable cause)
    {
        super("ImageGenerationError", errorMessage, cause);
    }
}
