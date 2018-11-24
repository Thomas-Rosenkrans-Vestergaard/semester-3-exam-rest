package com.group3.sem3exam.logic.images;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;


public class DataUriEncoder
{

    /**
     * Converts a byte image to a data uri.
     *
     * @param data The data to convert to a data uri.
     * @return The generated data uri.
     * @throws UnsupportedImageTypeException When the image type is not supported.
     * @see <a href="https://en.wikipedia.org/wiki/Data_URI_scheme">https://en.wikipedia.org/wiki/Data_URI_scheme</a>
     */
    public String bytesToDataURI(byte[] data) throws UnsupportedImageTypeException
    {
        String mimeType = getMimeType(data);
        ImageType.fromMime(mimeType);
        return String.format("data:%s;base64,%s", mimeType, Base64.getEncoder().encodeToString(data));
    }

    /**
     * Finds the mime type of the image in the provided byte data.
     *
     * @param data The image byte data to return the mime type of.
     * @return The mime type of the image in the provided byte data. {@code null} when the mime type could not be
     * read from the image byte data.
     */
    public String getMimeType(byte[] data)
    {
        try {
            InputStream is = new ByteArrayInputStream(data);
            return URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            return null;
        }
    }
}

