package com.group3.sem3exam.data.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ImagePost extends Post
{
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "imagepost", orphanRemoval = true)
    private List<ImagePostImage> images;

    public ImagePost(String title, String contents, User author, LocalDateTime createdAt, List<ImagePostImage> images)
    {
        super(title, contents, author, createdAt);
        this.images = images;
    }

    public ImagePost()
    {
    }
}
