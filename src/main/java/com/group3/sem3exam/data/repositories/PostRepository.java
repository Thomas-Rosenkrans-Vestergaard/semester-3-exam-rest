package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer>
{

    /**
     * Creates a new post entity using the provided information.
     *
     * @param user   The author of the new post.
     * @param body   The text body of the new post.
     * @param images The images to include in the post.
     * @param time   The time at which the post was made.
     * @return The newly created post.
     */
    Post create(User user, String body, List<Image> images, LocalDateTime time);

    /**
     * Returns the posts made by the user with the provided id.
     *
     * @param user The id of the user to return the posts of.
     * @return The posts made by the user with the provided id.
     */
    List<Post> getByAuthor(User user);

    /**
     * Returns a rolling view of the posts created by the provided user.
     *
     * @param user     The user to return the posts of.
     * @param pageSize The number of posts per page returned.
     * @param last     the offset from where to start to fetch posts.
     * @return The rolling view.
     */
    List<Post> getByAuthorRolling(User user, Integer pageSize, Integer last);

    /**
     * Returns a specific set of posts from the friends of the provided user.
     *
     * @param user     The id of the user to return the posts of.
     * @param pageSize the amount of new posts to fetch
     * @param last     the offset from where to start to fetch posts
     * @return The posts made by the friends of the user with the provided id.
     */
    List<Post> getTimeline(User user, Integer pageSize, Integer last);
}
