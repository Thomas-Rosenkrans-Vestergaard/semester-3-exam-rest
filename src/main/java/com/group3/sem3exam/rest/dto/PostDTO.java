package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;

import javax.ejb.Stateless;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Stateless
public class PostDTO
{

    private String        contents;
    private String        title;
    private LocalDateTime timeCreated;
    private User          author;

    public PostDTO(Post post)
    {
        this.contents = post.getContents();
        this.timeCreated = post.getCreatedAt();
        this.title = post.getTitle();
    }

    public PostDTO(Post post, boolean showUser){
        this.contents = post.getContents();
        this.timeCreated = post.getCreatedAt();
        this.title = post.getTitle();
        if(showUser){
            this.author = post.getAuthor();
        }


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

    public static PostDTO basic(Post post){
        return new PostDTO(post, false);
    }
}
