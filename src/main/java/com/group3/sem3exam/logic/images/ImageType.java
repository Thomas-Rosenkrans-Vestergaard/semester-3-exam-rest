package com.group3.sem3exam.logic.images;

public enum ImageType
{

    JPG("jpg"),
    PNG("png"),
    GIF("gif");

    /**
     * The extension of the {@link ImageType}.
     */
    private final String extension;

    /**
     * Creates a new {@link ImageType}.
     *
     * @param extension The extension of the {@link ImageType}.
     */
    ImageType(String extension)
    {
        this.extension = extension;
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
     * Returns the {@link ImageType} of the provided mime type.
     *
     * @param mime The mime to find the {@link ImageType} of.
     * @return The {@link ImageType} of the provided mime type.
     * @throws UnsupportedImageTypeException When the provided mime type cannot be converted to an {@link ImageType}.
     */
    public static ImageType fromMime(String mime) throws UnsupportedImageTypeException
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
                throw new UnsupportedImageTypeException();
        }
    }
}
