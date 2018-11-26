package com.group3.sem3exam.logic.images;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;

/**
 * Creates a thumbnail of a desired size from a provided buffered image.
 * <p>
 * The created thumbnail contains the largest amount of the source image.
 */
public class ImageThumbnailer
{

    /**
     * The target width of the thumbnails to create.
     */
    private final int targetWidth;

    /**
     * The target height of the thumbnails to create.
     */
    private final int targetHeight;

    /**
     * The aspect ratio of the target size. {@code aspect = targetWidth / targetHeight};
     */
    private final double aspect;

    /**
     * Creates a new {@link ImageThumbnailer}.
     *
     * @param targetWidth  The targetWidth of the thumbnails to create.
     * @param targetHeight The height of the thumbnails to create.
     */
    public ImageThumbnailer(int targetWidth, int targetHeight)
    {
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.aspect = (double) targetWidth / targetHeight;
    }

    /**
     * Creates a new thumbnail from the provided full image.
     *
     * @param full The full image to create a thumbnail from.
     * @return A buffered image thumbnail.
     * @throws ImageThumbnailerException When the thumbnail could not be generated.
     */
    public BufferedImage create(BufferedImage full) throws ImageThumbnailerException
    {

        if (full.getWidth() < this.targetWidth)
            throw new ImageThumbnailerException("The thumbnail could not be created, the image height must be >= " + this.targetHeight);
        if (full.getHeight() < this.targetHeight)
            throw new ImageThumbnailerException("The thumbnail could not be created, the image targetWidth must be >= " + this.targetWidth);


        long boundingX = 0;
        long boundingY = 0;
        long widthToScale;
        long heightToScale;

        int fullWidth  = full.getWidth();
        int fullHeight = full.getHeight();

        if (fullWidth > fullHeight * aspect) {
            heightToScale = full.getHeight();
            widthToScale = Math.round(heightToScale * aspect);
            boundingX = (fullWidth - widthToScale) / 2;
        } else {
            widthToScale = full.getWidth();
            heightToScale = Math.round(widthToScale / aspect);
            boundingY = (fullHeight - heightToScale) / 2;
        }

        BufferedImage resize = full.getSubimage(
                (int) boundingX,
                (int) boundingY,
                (int) widthToScale,
                (int) heightToScale
        );

        try {
            return Thumbnails.of(resize)
                             .forceSize(this.targetWidth, this.targetHeight)
                             .outputQuality(1f)
                             .asBufferedImage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
