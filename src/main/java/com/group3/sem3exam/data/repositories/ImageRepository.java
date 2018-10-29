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

    List<Image> getByUser(Integer user);

    List<Image> getByUserPaginated(Integer user, int pageSize, int pageNumber);
}
