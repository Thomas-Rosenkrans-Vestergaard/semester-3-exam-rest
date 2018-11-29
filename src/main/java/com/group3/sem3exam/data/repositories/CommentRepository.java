package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.CommentParent;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.ReadRepository;

import java.util.List;

public interface CommentRepository extends ReadRepository<Comment, Integer>
{

    /**
     * Returns the comment parent with the provided id.
     *
     * @param id The parent with the provided id.
     * @return The comment parent with the provided id, {@code null} when no such
     * record exits.
     */
    CommentParent getParent(Integer id);

    /**
     * Returns true if a comment parent with the provided id exists.
     *
     * @param parent The id of the comment parent to check for.
     * @return {@code}
     */
    boolean parentExists(Integer parent);

    /**
     * Returns all the comments on the provided comment parent.
     *
     * @param parent The parent to return comments from.
     * @return The comments on the provided comment parent.
     */
    List<Comment> getAll(CommentParent parent);

    /**
     * Creates a new comment, attaches the comment to the parent.
     *
     * @param user     The author of the comment.
     * @param contents The contents of the comment.
     * @param parent   The parent to attach the comment to.
     * @return The newly created comment.
     */
    Comment create(User user, String contents, CommentParent parent);
}
