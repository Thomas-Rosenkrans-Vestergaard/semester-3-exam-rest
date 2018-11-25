package com.group3.sem3exam.logic.images;

import com.group3.sem3exam.logic.FacadeException;

public class ImageThumbnailerException extends FacadeException
{

    public ImageThumbnailerException(String errorMessage)
    {
        this(errorMessage, null);
    }

    public ImageThumbnailerException(String errorMessage, Throwable cause)
    {
        super("ThumbnailGenerationError", errorMessage, cause);
    }
}
