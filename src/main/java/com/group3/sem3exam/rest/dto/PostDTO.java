package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Post;

import java.time.LocalDate;
import java.util.List;

public class PostDTO
{

    private String    contents;
    private LocalDate timeCreated;

    public PostDTO(String contents, LocalDate timeCreated)
    {
        this.contents = contents;
        this.timeCreated = timeCreated;
    }

    public PostDTO()
    {

    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public LocalDate getTimeCreated()
    {
        return timeCreated;
    }

    public void setTimeCreated(LocalDate timeCreated)
    {
        this.timeCreated = timeCreated;
    }
}
