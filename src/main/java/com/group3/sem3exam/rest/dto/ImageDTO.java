package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.GalleryImage;
import com.group3.sem3exam.data.entities.ProfilePicture;

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

    // Used by profile picture
    public String  src;

    public ImageDTO(GalleryImage image, boolean withUser)
    {
        this.id = image.getId();
        this.description = image.getDescription();
        this.full = image.getFull();
        this.thumbnail = image.getThumbnail();
        if (withUser)
            this.user = UserDTO.basic(image.getUser());
    }

    public ImageDTO(ProfilePicture profilePicture, boolean withUser)
    {
        if (profilePicture != null) {
            this.src = profilePicture.getSrc();
            if (withUser)
                this.user = UserDTO.basic(profilePicture.getUser());
        }
    }

    public static ImageDTO basic(GalleryImage image)
    {
        return new ImageDTO(image, false);
    }

    public static ImageDTO basic(ProfilePicture profilePicture)
    {
        return new ImageDTO(profilePicture, false);
    }

    public static List<ImageDTO> list(List<GalleryImage> images, Function<GalleryImage, ImageDTO> f)
    {
        List<ImageDTO> results = new ArrayList<>();
        for (GalleryImage image : images)
            results.add(f.apply(image));

        return results;
    }
}

