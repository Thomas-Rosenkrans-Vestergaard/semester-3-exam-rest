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
    private String title;

    @Column(nullable = false)
    @Lob
    private byte[] data;

    @ManyToOne(fetch = EAGER, optional = false)
    private User user;

    public Image()
    {

    }

    public Image(String title, byte[] data, User user)
    {
        this.title = title;
        this.data = data;
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

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public byte[] getData()
    {
        return this.data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
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
