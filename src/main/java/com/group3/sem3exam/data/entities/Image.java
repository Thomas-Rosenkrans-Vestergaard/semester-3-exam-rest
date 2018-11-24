package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.RepositoryEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "image")
public class Image implements RepositoryEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(nullable = false)
    private String full;

    @Lob
    @Column
    private String thumbnail;

    @ManyToOne(fetch = EAGER, optional = false)
    private User user;

    public Image()
    {

    }

    public Image(String description, String full, String thumbnail, User user)
    {
        this.description = description;
        this.full = full;
        this.thumbnail = thumbnail;
        this.user = user;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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
