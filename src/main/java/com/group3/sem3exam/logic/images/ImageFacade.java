package com.group3.sem3exam.logic.images;

import com.group3.sem3exam.data.entities.GalleryImage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.ImageRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.ResourceNotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.group3.sem3exam.logic.images.ImageType.fromData;

public class ImageFacade<T extends Transaction>
{

    /**
     * The factory that produces transaction of type {@code T}.
     */
    private final Supplier<T> transactionFactory;

    /**
     * The factory that create User repositories from the provided {@code T}.
     */
    private final Function<T, UserRepository> userRepositoryFactory;

    /**
     * The factory that create Image repositories from the provided {@code T}.
     */
    private final Function<T, ImageRepository> imageRepositoryFactory;

    /**
     * Creates a new {@link ImageFacade}.
     *
     * @param transactionFactory     The factory that produces transaction of type {@code T}.
     * @param userRepositoryFactory  The factory that create User repositories from the provided {@code T}.
     * @param imageRepositoryFactory The factory that create Image repositories from the provided {@code T}.
     */
    public ImageFacade(
            Supplier<T> transactionFactory,
            Function<T, UserRepository> userRepositoryFactory,
            Function<T, ImageRepository> imageRepositoryFactory
    )
    {
        this.transactionFactory = transactionFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.imageRepositoryFactory = imageRepositoryFactory;
    }

    /**
     * Returns the image with the provided id.
     *
     * @param id The id of the image to retrieve.
     * @return The image with the provided id.
     * @throws ResourceNotFoundException When an image with the provided id does not exist.
     */
    public GalleryImage get(Integer id) throws ResourceNotFoundException
    {
        try (ImageRepository ir = imageRepositoryFactory.apply(transactionFactory.get())) {
            GalleryImage image = ir.get(id);
            if (image == null)
                throw new ResourceNotFoundException(GalleryImage.class, id);

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
    public List<GalleryImage> getByUser(Integer user) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            UserRepository  ur = userRepositoryFactory.apply(transaction);
            ImageRepository ir = imageRepositoryFactory.apply(transaction);
            if (!ur.exists(user))
                throw new ResourceNotFoundException(User.class, user);

            return ir.getByUser(user);
        }
    }

    /**
     * Returns the number of images by the user with the provided id.
     *
     * @param user The id of the user to return the number of images from.
     * @return The number of images by the user with the provided id.
     * @throws ResourceNotFoundException When the user with the provided id does not exist.
     */
    public int countByUser(Integer user) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            UserRepository  ur            = userRepositoryFactory.apply(transaction);
            ImageRepository ir            = imageRepositoryFactory.apply(transaction);
            User            retrievedUser = ur.get(user);
            if (retrievedUser == null)
                throw new ResourceNotFoundException(User.class, user);

            return ir.countByUser(retrievedUser);
        }
    }

    /**
     * Returns a paginated view of the images by the user with the provided id.
     *
     * @param user       The id of the user to return the image of.
     * @param pageSize   The number of results per page. Where {@code pageSize >= 0}.
     * @param pageNumber The page number to retrieve. Where {@code pageNumber > 0}.
     * @return The images by the user with the provided id.
     * @throws ResourceNotFoundException When a user with the provided id does not exist.
     */
    public List<GalleryImage> getByUserPaginated(Integer user, int pageSize, int pageNumber)
    throws ResourceNotFoundException
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        try (T transaction = transactionFactory.get()) {
            UserRepository  ur = userRepositoryFactory.apply(transaction);
            ImageRepository ir = imageRepositoryFactory.apply(transaction);
            if (!ur.exists(user))
                throw new ResourceNotFoundException(User.class, user);

            return ir.getByUserPaginated(user, pageSize, pageNumber);
        }
    }

    /**
     * Saves an image with the provided information to the database.
     *
     * @param user        The owner of the image.
     * @param description The description of the image.
     * @param base64      The base64 encoded string containing the data in the image.
     * @return The created image.
     * @throws UnsupportedImageFormatException When the provided image is of an unsupported type.
     * @throws ImageThumbnailerException       When a thumbnail of the image cannot be created.
     */
    public GalleryImage create(User user, String description, String base64)
    throws UnsupportedImageFormatException, ImageThumbnailerException
    {
        DataUriEncoder uriEncoder = new DataUriEncoder();
        byte[]         data       = Base64.getDecoder().decode(base64);
        ImageType      type       = fromData(data);
        String         full       = uriEncoder.encode(data, type);
        String         thumbnail  = createThumbnail(data, type);

        try (ImageRepository ir = imageRepositoryFactory.apply(transactionFactory.get())) {
            ir.begin();
            GalleryImage image = ir.create(description, full, thumbnail, user);
            ir.commit();
            return image;
        }
    }

    /**
     * Creates a thumbnail data URI from the provided full image data.
     *
     * @param data The data from the full image.
     * @return The resulting thumbnail data uri.
     * @throws ImageThumbnailerException When the thumbnail cannot be created.
     */
    private String createThumbnail(byte[] data, ImageType type) throws ImageThumbnailerException
    {
        try {
            ImageThumbnailer thumbnailMaker = new ImageThumbnailer(250, 250);
            BufferedImage    full           = ImageIO.read(new ByteArrayInputStream(data));
            BufferedImage    thumbnail      = thumbnailMaker.create(full);
            DataUriEncoder   encoder        = new DataUriEncoder();
            return encoder.encode(thumbnail, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}