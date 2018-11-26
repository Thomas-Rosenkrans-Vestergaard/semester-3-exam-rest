package com.group3.sem3exam.logic.images;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageCropperTest
{

    private static String path        = "./src/test/java/com/group3/sem3exam/logic/images/";
    private static File   EXAMPLE_PNG = new File(path + "example.png");

    @Test
    void crop() throws IOException, ImageCropperException
    {
        CropArea      crop    = new CropArea(200, 150, 400, 300);
        BufferedImage full    = ImageIO.read(EXAMPLE_PNG);
        ImageCropper  cropper = new ImageCropper(crop);
        BufferedImage result  = cropper.crop(full);

        assertEquals(400, result.getWidth());
        assertEquals(300, result.getHeight());
        ImageIO.write(result, "png", new FileOutputStream(path + "crop_output.png"));
    }
}