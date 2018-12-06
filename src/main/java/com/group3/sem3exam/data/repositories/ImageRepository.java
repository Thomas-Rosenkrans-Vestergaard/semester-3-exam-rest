package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.CrudRepository;

import java.util.List;

/**
 * Represents a data source of images. Defines read and write operations on the data source.
 */
public interface ImageRepository extends CrudRepository<Image, Integer>
{

    /**
     * Creates a new image entity from the provided information.
     *
     * @param description The description of the new image.
     * @param full        The URI of the new image.
     * @param thumbnail   The URI of the thumbnail.
     * @param user        The owner of the image.
     * @return The newly created image.
     */
    Image create(String description, String full, String thumbnail, User user);

    /**
     * Returns all the images of the user with the provided id.
     *
     * @param user The user to return the images of.
     * @return The images of the user with the provided id.
     */
    List<Image> getByUser(User user);

    /**
     * Returns the number of images by the provided user.
     *
     * @param user The user to return the number of images from.
     * @return The number of images by the provided user.
     */
    int countByUser(User user);

    /**
     * Returns a paginated view of the images of the provided user.
     *
     * @param user       The user to return the images of.
     * @param pageSize   The number of images in a single page in the paginated view.
     * @param pageNumber The page number to retrieve. Starts at 1.
     * @return The paginated view.
     */
    List<Image> getByUserPaginated(User user, int pageSize, int pageNumber);
}
