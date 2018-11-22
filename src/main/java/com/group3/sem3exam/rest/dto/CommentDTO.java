package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Comment;

import java.time.LocalDateTime;

public class CommentDTO
{
    public Integer       id;
    public String        contents;
    public LocalDateTime createdAt;
    public Integer       authorId;
    public Integer       postId;

    public CommentDTO(Comment comment)
    {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.authorId = comment.getAuthor().getId();
        this.postId = comment.getPost().getId();
    }
}
