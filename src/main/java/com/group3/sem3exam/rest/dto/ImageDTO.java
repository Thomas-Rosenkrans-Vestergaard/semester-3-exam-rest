package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;

public class ImageDTO
{

    private Integer id;
    private String title;
    private byte[] data;
    private User user;


    public ImageDTO(Image image, boolean withUser){
        this.id = image.getId();
        this.title = image.getTitle();
        this.data = image.getData();
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

    public byte[] getData()
    {
        return data;
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

