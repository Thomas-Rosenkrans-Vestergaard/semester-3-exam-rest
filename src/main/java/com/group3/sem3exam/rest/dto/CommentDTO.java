package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CommentDTO
{

    public final Integer id;
    public final String  contents;
    public final LocalDateTime createdAt;
    public final UserDTO author;
    public String emoji = null;
    public final Integer count;

    private CommentDTO(Comment comment, boolean withEmojis)
    {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.author = new UserDTO(comment.getAuthor());
        if(withEmojis)
            this.emoji = comment.getEmoji();
            this.count = comment.getCount();         }

    public static CommentDTO basic(Comment comment)
    {
        return new CommentDTO(comment, false);
    }

    public static CommentDTO withEmoji(Comment comment){
        return new CommentDTO(comment, true);
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
