package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer>
{


    /**
     * Creates a new post entity using the provided information.
     *
     * @param user  The author of the new post.
     * @param title The title of the new post.
     * @param body  The text body of the new post.
     * @param time  The time at which the post was made.
     * @return The newly created post.
     */
    Post createPost(User user, String title, String body, LocalDateTime time);

    /**
     * Returns the timeline for the provided user.
     *
     * @param user The id of the user to return the timeline of.
     * @return The posts on the timeline for the provided user.
     */
    List<Post> getTimeline(Integer user);

    /**
     * Returns the posts made by the user with the provided id.
     *
     * @param user The id of the user to return the posts of.
     * @return The posts made by the user with the provided id.
     */
    List<Post> getByUserId(User user);
}
