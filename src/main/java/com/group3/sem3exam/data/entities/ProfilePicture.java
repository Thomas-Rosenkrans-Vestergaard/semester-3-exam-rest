package com.group3.sem3exam.data.entities;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "profile_image")
public class ProfilePicture
{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Lob
    @Column(nullable = false)
    private String src;

    @OneToOne(optional = false, fetch = LAZY)
    private User user;

    public ProfilePicture()
    {

    }

    public ProfilePicture(String src, User user)
    {
        this.src = src;
        this.user = user;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getSrc()
    {
        return this.src;
    }

    public void setSrc(String src)
    {
        this.src = src;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
