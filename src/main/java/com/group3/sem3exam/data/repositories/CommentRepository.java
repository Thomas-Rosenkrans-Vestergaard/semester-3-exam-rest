package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Comment;

import java.util.List;

public interface CommentRepository extends ReadCrudRepository<Comment, Integer>
{

    /**
     * Returns a complete list of comments for a given post
     *
     * @param PostId Id of the post which to return comments from
     * @return The complete list of the comments from the post
     */

    List<Comment> getCommentsByPost(Integer postId);

}
