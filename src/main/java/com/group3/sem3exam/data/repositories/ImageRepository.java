package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;

import java.util.List;

/**
 * Represents a data source of images. Defines read and write operations on the data source.
 */
public interface ImageRepository extends CrudRepository<Image, Integer>
{

    /**
     * Creates a new image entity from the provided information.
     *
     * @param title The title of the new image.
     * @param uri   The URI of the new image.
     * @param user  The owner of the image.
     * @return The newly created image.
     */
    Image create(String title, String uri, User user);

    /**
     * Returns all the images of the user with the provided id.
     *
     * @param user The id of the user to return the images of.
     * @return The images of the user with the provided id.
     */
    List<Image> getByUser(Integer user);

    /**
     * Returns the number of images by the provided user.
     *
     * @param user The user to return the number of images from.
     * @return The number of images by the provided user.
     */
    int countByUser(User user);

    /**
     * Returns a paginated view of the images of the user with the provided id.
     *
     * @param user       The user to return the images of.
     * @param pageSize   The number of images in a single page in the paginated view.
     * @param pageNumber The page number to retrieve. Starts at 1.
     * @return The paginated view.
     */
    List<Image> getByUserPaginated(Integer user, int pageSize, int pageNumber);
}
