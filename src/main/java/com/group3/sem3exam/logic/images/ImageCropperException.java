package com.group3.sem3exam.logic.images;

import com.group3.sem3exam.logic.FacadeException;

public class ImageCropperException extends FacadeException
{

    public ImageCropperException(String errorMessage)
    {
        this(errorMessage, null);
    }

    public ImageCropperException(String errorMessage, Throwable cause)
    {
        super("ImageCropperError", errorMessage, cause);
    }
}
