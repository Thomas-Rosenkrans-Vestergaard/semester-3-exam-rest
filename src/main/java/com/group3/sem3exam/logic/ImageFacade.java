package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.ImageRepository;
import com.group3.sem3exam.logic.images.DataUriEncoder;
import com.group3.sem3exam.logic.images.UnsupportedImageTypeException;

import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;

public class ImageFacade
{

    /**
     * The factory that produces instances the {@link ImageRepository} used by this facade.
     */
    private final Supplier<ImageRepository> imageRepositoryFactory;

    /**
     * Creates a new {@link ImageFacade}.
     *
     * @param imageRepositoryFactory The factory that produces instances the {@link ImageRepository} used by this facade.
     */
    public ImageFacade(Supplier<ImageRepository> imageRepositoryFactory)
    {
        this.imageRepositoryFactory = imageRepositoryFactory;
    }

    /**
     * Returns the image with the provided id.
     *
     * @param id The id of the image to retrieve.
     * @return The image with the provided id.
     * @throws ResourceNotFoundException When an image with the provided id does not exist.
     */
    public Image get(Integer id) throws ResourceNotFoundException
    {

        try (ImageRepository ir = imageRepositoryFactory.get()) {
            Image image = ir.get(id);
            if (image == null)
                throw ResourceNotFoundException.with404(Image.class, id);

            return image;
        }
    }

    /**
     * Returns the images by the user with the provided id.
     *
     * @param user The id of the user to return the image of.
     * @return The images by the user with the provided id.
     * @throws ResourceNotFoundException When a user with the provided id does not exist.
     */
    public List<Image> getByUser(Integer user) throws ResourceNotFoundException
    {
        try (ImageRepository ir = imageRepositoryFactory.get()) {
            List<Image> images = ir.getByUser(user);
            if (images == null)
                throw ResourceNotFoundException.with404(User.class, user);

            return images;
        }
    }

    /**
     * Saves an image with the provided information to the database.
     *
     * @param user   The owner of the image.
     * @param title  The title of the image.
     * @param base64 The base64 encoded string containing the data in the image.
     * @return The created image.
     * @throws UnsupportedImageTypeException When the image type is not supported.
     */
    public Image create(User user, String title, String base64) throws UnsupportedImageTypeException
    {
        DataUriEncoder uriEncoder = new DataUriEncoder();
        byte[]         data       = Base64.getDecoder().decode(base64);
        String         dataUri    = uriEncoder.bytesToDataURI(data);

        try (ImageRepository ir = imageRepositoryFactory.get()) {
            ir.begin();
            Image image = ir.create(title, dataUri, user);
            ir.commit();
            return image;
        }
    }
}