package com.group3.sem3exam.data.entities;

import javax.persistence.*;

@Entity
public class ProfilePicture
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;

    @OneToOne
    private Image image;

    public ProfilePicture()
    {

    }

    public ProfilePicture(User user, Image image)
    {
        this.user = user;
        this.image = image;
    }

    public String getDescription()
    {
        return image.getDescription();
    }

    public void setDescription(String description)
    {
        image.setDescription(description);
    }

    public String getFull()
    {
        return image.getFull();
    }

    public void setFull(String full)
    {
        image.setFull(full);
    }

    public String getThumbnail()
    {
        return image.getThumbnail();
    }

    public void setThumbnail(String thumbnail)
    {
        image.setThumbnail(thumbnail);
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Image getImage()
    {
        return this.image;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }
}
