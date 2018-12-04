package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PostDTO
{

    public final Integer        id;
    public final String         contents;
    public final LocalDateTime  timeCreated;
    public final PostAuthorDTO  author;
    public final List<ImageDTO> images;


    private PostDTO(Integer id, String contents, LocalDateTime timeCreated, PostAuthorDTO author, List<ImageDTO> images)
    {
        this.id = id;
        this.contents = contents;
        this.timeCreated = timeCreated;
        this.author = author;
        this.images = images;
    }

    public static PostDTO basic(Post post)
    {
        return new PostDTO(
                post.getId(),
                post.getContents(),
                post.getCreatedAt(),
                null,
                ImageDTO.list(post.getImages(), ImageDTO::withoutUser)
        );
    }

    public static PostDTO withAuthor(Post post)
    {
        User author = post.getAuthor();

        return new PostDTO(

                post.getId(),
                post.getContents(),
                post.getCreatedAt(),
                new PostAuthorDTO(
                        author.getId(),
                        author.getName(),
                        author.getProfilePicture() == null ? null : author.getProfilePicture().getThumbnail()
                ),
                ImageDTO.list(post.getImages(), ImageDTO::withoutUser)
        );
    }

    public static List<PostDTO> list(List<Post> posts, Function<Post, PostDTO> f)
    {
        List<PostDTO> result = new ArrayList<>(posts.size());
        for (Post post : posts) {
            result.add(f.apply(post));
        }
        return result;
    }

    private static class PostAuthorDTO
    {
        public final Integer id;
        public final String  name;
        public final String  profilePicture;

        public PostAuthorDTO(Integer id, String name, String profilePicture)
        {
            this.id = id;
            this.name = name;
            this.profilePicture = profilePicture;
        }
    }
}
