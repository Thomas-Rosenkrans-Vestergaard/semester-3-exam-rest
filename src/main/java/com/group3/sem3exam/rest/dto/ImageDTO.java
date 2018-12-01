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
    public String  description;
    public String  full;
    public String  thumbnail;
    public UserDTO user;

    public ImageDTO(Integer id, String description, String full, String thumbnail, UserDTO user)
    {
        this.id = id;
        this.description = description;
        this.full = full;
        this.thumbnail = thumbnail;
        this.user = user;
    }

    public static ImageDTO complete(Image image)
    {
        if (image == null)
            return null;

        return new ImageDTO(
                image.getId(),
                image.getDescription(),
                image.getFull(),
                image.getThumbnail(),
                UserDTO.publicView(image.getUser())
        );
    }

    public static ImageDTO thumbnail(Image image)
    {
        if (image == null)
            return null;

        return new ImageDTO(
                image.getId(),
                image.getDescription(),
                null,
                image.getThumbnail(),
                UserDTO.publicView(image.getUser())
        );
    }

    public static List<ImageDTO> list(List<Image> images, Function<Image, ImageDTO> f)
    {
        List<ImageDTO> results = new ArrayList<>();
        for (Image image : images)
            results.add(f.apply(image));

        return results;
    }
}

