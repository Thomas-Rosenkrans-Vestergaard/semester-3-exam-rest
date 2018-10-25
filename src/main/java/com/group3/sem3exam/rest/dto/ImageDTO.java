package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;

import javax.ejb.Stateless;


@Stateless
public class ImageDTO
{

    private Integer id;
    private String title;
    private String uri;
    private User user;


    public ImageDTO(Image image, boolean withUser){
        this.id = image.getId();
        this.title = image.getTitle();
        this.uri = image.getUri();
        if(withUser)
            this.user = image.getUser();
    }

    public static ImageDTO basic(Image image){
        return new ImageDTO(image, true);
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
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUri()
    {
        return uri;
    }

    public void setData(String uri)
    {
        this.uri = uri;
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

