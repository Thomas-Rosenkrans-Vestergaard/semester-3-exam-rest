package com.group3.sem3exam.logic.images;

import java.awt.image.BufferedImage;

/**
 * Crops a provided image to a desired size.
 */
public class ImageCropper
{

    /**
     * The crop dimensions.
     */
    private final CropArea crop;

    /**
     * Creates a new {@link ImageCropper}.
     *
     * @param crop The crop dimensions.
     */
    public ImageCropper(CropArea crop)
    {
        this.crop = crop;
    }

    /**
     * Crops the provided {@code full} image, returning a cropped version.
     *
     * @param full The full image to crop.
     * @return The cropped version of the provided image.
     * @throws ImageCropperException When the provided image cannot be cropped to the configured size.
     */
    public BufferedImage crop(BufferedImage full) throws ImageCropperException
    {
        if (full.getWidth() < this.crop.getWidth())
            throw new ImageCropperException("The provided image is not wide enough to be cropped.");
        if (full.getHeight() < this.crop.getHeight())
            throw new ImageCropperException("The provided image is not wide enough to be cropped.");
        if (full.getWidth() < this.crop.getRightEdge())
            throw new ImageCropperException("The provided crop area is too wide for the image.");
        if (full.getHeight() < this.crop.getBottomEdge())
            throw new ImageCropperException("The provided crop area is too tall for the image.");

        return full.getSubimage(
                crop.getX(),
                crop.getY(),
                crop.getWidth(),
                crop.getHeight()
        );
    }
}
