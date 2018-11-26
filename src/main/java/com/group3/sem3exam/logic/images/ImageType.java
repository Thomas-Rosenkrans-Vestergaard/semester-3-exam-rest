package com.group3.sem3exam.logic.images;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public enum ImageType
{

    JPG("jpg", "image/jpg"),
    PNG("png", "image/png"),
    GIF("gif", "image/gif");

    /**
     * The extension of the {@link ImageType}.
     */
    private final String extension;

    /**
     * The mime type of the {@link ImageType}.
     */
    private final String mime;

    /**
     * Creates a new {@link ImageType}.
     *
     * @param extension The extension of the {@link ImageType}.
     * @param mime      The mime type of the {@link ImageType}.
     */
    ImageType(String extension, String mime)
    {
        this.extension = extension;
        this.mime = mime;
    }

    /**
     * Returns the extension of the {@link ImageType}.
     *
     * @return The extension of the {@link ImageType}.
     */
    public String getExtension()
    {
        return this.extension;
    }

    /**
     * Returns the mime type of the {@link ImageType}.
     *
     * @return The mime type of the {@link ImageType}.
     */
    public String getMime()
    {
        return this.mime;
    }

    /**
     * Returns the {@link ImageType} of the provided mime type.
     *
     * @param mime The mime to find the {@link ImageType} of.
     * @return The {@link ImageType} of the provided mime type.
     * @throws UnsupportedImageFormatException When the provided mime type cannot be converted to an {@link ImageType}.
     */
    public static ImageType fromMime(String mime) throws UnsupportedImageFormatException
    {
        switch (mime) {
            case "image/jpg":
                return ImageType.JPG;
            case "image/jpeg":
                return ImageType.JPG;
            case "image/gif":
                return ImageType.GIF;
            case "image/png":
                return ImageType.PNG;
            default:
                throw new UnsupportedImageFormatException();
        }
    }

    /**
     * Finds the image type of the image in the provided byte data.
     *
     * @param data The image byte data to return the mime type of.
     * @return The image type of the image in the provided byte data. {@code null} when the mime type could not be
     * read from the image byte data.
     * @throws UnsupportedImageFormatException When the data cannot be read.
     */
    public static ImageType fromData(byte[] data) throws UnsupportedImageFormatException
    {
        try {
            InputStream is   = new ByteArrayInputStream(data);
            String      mime = URLConnection.guessContentTypeFromStream(is);
            if (mime == null)
                throw new UnsupportedImageFormatException();
            return ImageType.fromMime(mime);
        } catch (IOException e) {
            throw new UnsupportedImageFormatException();
        }
    }
}
