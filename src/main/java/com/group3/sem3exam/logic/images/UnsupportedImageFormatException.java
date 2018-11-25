package com.group3.sem3exam.logic.images;

import com.group3.sem3exam.logic.FacadeException;

public class UnsupportedImageFormatException extends FacadeException
{

    public UnsupportedImageFormatException()
    {
        super("UnsupportedImageFormatError", "The provided image is of an unsupported type.", null);
    }
}
