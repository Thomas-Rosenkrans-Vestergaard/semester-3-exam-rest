package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post extends CommentParent implements RepositoryEntity<Integer>
{

    @Column(length = 65535, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(optional = false)
    private User author;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public Post()
    {
        super();
    }

    public Post(String contents, List<Image> images, User author, LocalDateTime createdAt)
    {
        super();
        this.contents = contents;
        this.images = images;
        this.author = author;
        this.createdAt = createdAt;
    }

    public String getContents()
    {
        return this.contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public User getAuthor()
    {
        return this.author;
    }

    public void setAuthor(User author)
    {
        this.author = author;
    }

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public List<Image> getImages()
    {
        return this.images;
    }

    public Post setImages(List<Image> images)
    {
        this.images = images;
        return this;
    }
}
