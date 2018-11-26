package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class ImagePostImage implements RepositoryEntity<Integer>
{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Lob
    @Column(nullable = false)
    private String data;

    @Lob
    @Column
    private String thumbnail;

    @ManyToOne(fetch = EAGER, optional = false)
    private User user;

    public ImagePostImage(String data, String thumbnail, User user)
    {
        this.data = data;
        this.thumbnail = thumbnail;
        this.user = user;
    }

    public ImagePostImage()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getThumbnail()
    {
        return thumbnail;
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
