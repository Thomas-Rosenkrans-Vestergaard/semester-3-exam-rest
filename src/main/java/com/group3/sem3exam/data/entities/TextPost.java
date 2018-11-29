package com.group3.sem3exam.data.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;


@Entity
@DiscriminatorValue(value = "t")
public class TextPost extends Post
{

    public TextPost(String contents, User author, LocalDateTime createdAt)
    {
        super(contents, author, createdAt);
    }

    public TextPost()
    {
    }
}
