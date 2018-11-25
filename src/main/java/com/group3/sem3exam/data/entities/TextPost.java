package com.group3.sem3exam.data.entities;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity(name="TextPost")
public class TextPost extends Post
{
    public TextPost(String title, String contents, User author, LocalDateTime createdAt)
    {
        super(title, contents, author, createdAt);
    }

    public TextPost()
    {
    }
}
