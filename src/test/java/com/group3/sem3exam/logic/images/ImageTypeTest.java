package com.group3.sem3exam.logic.images;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.group3.sem3exam.logic.images.ImageType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImageTypeTest
{

    private static String path        = "./src/test/java/com/group3/sem3exam/logic/images/";
    private static File   EXAMPLE_GIF = new File(path + "example.gif");
    private static File   EXAMPLE_JPG = new File(path + "example.jpg");
    private static File   EXAMPLE_PNG = new File(path + "example.png");

    @Test
    void getExtension()
    {
        assertEquals("png", PNG.getExtension());
        assertEquals("jpg", JPG.getExtension());
        assertEquals("gif", GIF.getExtension());
    }

    @Test
    void getMime()
    {
        assertEquals("image/png", PNG.getMime());
        assertEquals("image/jpg", JPG.getMime());
        assertEquals("image/gif", GIF.getMime());
    }

    @Test
    void fromMime() throws UnsupportedImageFormatException
    {
        assertEquals(PNG, ImageType.fromMime("image/png"));
        assertEquals(JPG, ImageType.fromMime("image/jpg"));
        assertEquals(GIF, ImageType.fromMime("image/gif"));
    }

    @Test
    void fromMimeThrowsUnsupportedImageFormatException()
    {
        assertThrows(UnsupportedImageFormatException.class, () -> {
            ImageType.fromMime("image/does_not_exist");
        });
    }

    @Test
    void fromData() throws IOException, UnsupportedImageFormatException
    {
        assertEquals(PNG, ImageType.fromData(getData(EXAMPLE_PNG)));
        assertEquals(JPG, ImageType.fromData(getData(EXAMPLE_JPG)));
        assertEquals(GIF, ImageType.fromData(getData(EXAMPLE_GIF)));
    }

    @Test
    void fromDataThrowsUnsupportedImageFormatException()
    {
        assertThrows(UnsupportedImageFormatException.class, () -> {
            ImageType.fromData(new byte[1024]);
        });
    }

    byte[] getData(File file) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Files.copy(file.toPath(), outputStream);
        return outputStream.toByteArray();
    }
}