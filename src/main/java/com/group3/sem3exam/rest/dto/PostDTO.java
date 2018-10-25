package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Post;

import javax.ejb.Stateless;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Stateless
public class PostDTO
{

    private String        contents;
    private LocalDateTime timeCreated;

    public PostDTO(Post post)
    {
        this.contents = post.getContents();
        this.timeCreated = post.getCreatedAt();
    }


    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public LocalDateTime getTimeCreated()
    {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated)
    {
        this.timeCreated = timeCreated;
    }
}
