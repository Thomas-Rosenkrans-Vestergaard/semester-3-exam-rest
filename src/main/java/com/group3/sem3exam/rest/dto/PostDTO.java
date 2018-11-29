package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Post;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Stateless
public class PostDTO
{

    public final Integer       id;
    public final String        contents;
    public final LocalDateTime timeCreated;
    public final UserDTO       author;

    public PostDTO(Post post)
    {
        this(post, false);
    }

    public PostDTO(Post post, boolean showAuthor)
    {
        this.id = post.getId();
        this.contents = post.getContents();
        this.timeCreated = post.getCreatedAt();
        this.author = showAuthor ? UserDTO.basic(post.getAuthor()) : null;
    }

    public static PostDTO basic(Post post)
    {
        return new PostDTO(post, false);
    }

    public static PostDTO withAuthor(Post post)
    {
        return new PostDTO(post, true);
    }

    public static List<PostDTO> list(List<Post> posts, Function<Post, PostDTO> f)
    {
        List<PostDTO> result = new ArrayList<>(posts.size());
        for (Post post : posts) {
            result.add(f.apply(post));
        }
        return result;
    }
}
