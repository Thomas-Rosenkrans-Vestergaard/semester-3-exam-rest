package com.group3.sem3exam.data.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "i")
public class ImagePost extends Post
{

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagePostImage> images = new ArrayList<>();

    public ImagePost(String contents, User author, LocalDateTime createdAt, List<ImagePostImage> images)
    {
        super(contents, author, createdAt);
        this.images = images;
    }

    public ImagePost()
    {
    }

    public List<ImagePostImage> getImages()
    {
        return this.images;
    }

    public void setImages(List<ImagePostImage> images)
    {
        this.images = images;
    }
}
