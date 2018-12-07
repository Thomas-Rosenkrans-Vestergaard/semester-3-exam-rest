package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "image")
public class Image extends CommentParent implements RepositoryEntity<Integer>
{

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(nullable = false, name = "full_")
    private String full;

    @Lob
    @Column
    private String thumbnail;

    @ManyToOne(fetch = EAGER, optional = false)
    private User user;

    public Image()
    {
        super();
    }

    public Image(String description, String full, String thumbnail, User user)
    {
        super();
        this.description = description;
        this.full = full;
        this.thumbnail = thumbnail;
        this.user = user;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getFull()
    {
        return this.full;
    }

    public void setFull(String full)
    {
        this.full = full;
    }

    public String getThumbnail()
    {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
