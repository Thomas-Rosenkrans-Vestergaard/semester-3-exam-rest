package com.group3.sem3exam.logic.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Encodes image data to data uri.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Data_URI_scheme">https://en.wikipedia.org/wiki/Data_URI_scheme</a>
 */
public class DataUriEncoder
{

    /**
     * Converts a image byte array to a data uri.
     *
     * @param data The data to convert to a data uri.
     * @param type The type of the image to convert.
     * @return The generated data uri.
     */
    public String encode(byte[] data, ImageType type)
    {
        return String.format("data:%s;base64,%s", type.getMime(), Base64.getEncoder().encodeToString(data));
    }

    /**
     * Converts a provided buffered image to a data uri.
     *
     * @param bufferedImage The image to convert to a data uri.
     * @param type          The type of the image to convert.
     * @return The generated data uri.
     */
    public String encode(BufferedImage bufferedImage, ImageType type)
    {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, type.getExtension(), outputStream);
            return encode(outputStream.toByteArray(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

