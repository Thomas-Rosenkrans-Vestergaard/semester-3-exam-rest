package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CommentDTO
{

    public final Integer id;
    public final String  contents;
    public final UserDTO author;

    private CommentDTO(Comment comment)
    {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.author = new UserDTO(comment.getAuthor());
    }

    public static CommentDTO basic(Comment comment)
    {
        return new CommentDTO(comment);
    }

    public static List<CommentDTO> list(List<Comment> comments, Function<Comment, CommentDTO> f)
    {
        List<CommentDTO> results = new ArrayList<>();
        for (Comment comment : comments)
            results.add(f.apply(comment));

        return results;
    }

    private static final class UserDTO
    {
        public Integer id;
        public String  name;
        public String  profilePicture;

        public UserDTO(User user)
        {
            this.id = user.getId();
            this.name = user.getName();
            this.profilePicture = user.getProfilePicture() == null ? null : user.getProfilePicture().getThumbnail();
        }
    }
}
