package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Image;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Stateless
public class ImageDTO
{

    public Integer id;
    public String  title;
    public String  uri;
    public UserDTO user;

    public ImageDTO(Image image, boolean withUser)
    {
        this.id = image.getId();
        this.title = image.getTitle();
        this.uri = image.getUri();
        if (withUser)
            this.user = UserDTO.basic(image.getUser());
    }

    public static ImageDTO basic(Image image)
    {
        return new ImageDTO(image, true);
    }

    public static List<ImageDTO> list(List<Image> images, Function<Image, ImageDTO> f)
    {
        List<ImageDTO> results = new ArrayList<>();
        for (Image image : images)
            results.add(f.apply(image));

        return results;
    }
}

