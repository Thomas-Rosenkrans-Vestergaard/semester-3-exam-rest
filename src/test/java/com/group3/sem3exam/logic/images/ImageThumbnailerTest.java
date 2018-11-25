package com.group3.sem3exam.logic.images;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageThumbnailerTest
{

    private static String path        = "./src/test/java/com/group3/sem3exam/logic/images/";
    private static File   EXAMPLE_PNG = new File(path + "example.png");

    @Test
    void create() throws IOException, ImageThumbnailerException
    {
        ImageThumbnailer thumbnailer = new ImageThumbnailer(200, 500);
        BufferedImage    full        = ImageIO.read(EXAMPLE_PNG);
        BufferedImage    result      = thumbnailer.create(full);

        assertEquals(200, result.getWidth());
        assertEquals(500, result.getHeight());
        ImageIO.write(result, "png", new FileOutputStream(path + "thumb_output.png"));
    }
}