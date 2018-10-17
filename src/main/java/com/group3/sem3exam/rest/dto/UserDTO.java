package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO
{

    private Integer             id;
    private String              name;
    private String              email;
    private City                city;
    private Gender              gender;
    private LocalDate           dateOfBirth;
    private LocalDateTime       createdAt;
    private List<PostDTO>       posts       = new ArrayList<>();
    private List<FriendshipDTO> friendships = new ArrayList<>();

    public UserDTO(User user, boolean showFriendships, boolean showPosts)
    {
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.city = user.getCity();
        this.gender = user.getGender();
        this.dateOfBirth = user.getDateOfBirth();
        if (showFriendships)
            this.friendships = user.getFriendships()
                                   .stream()
                                   .map(friendship -> new FriendshipDTO(friendship))
                                   .collect(Collectors.toList());
        if (showPosts)
            this.posts = user.getPosts()
                             .stream()
                             .map(posts -> new PostDTO(posts))
                             .collect(Collectors.toList());
    }

    public static UserDTO basic(User user)
    {
        return new UserDTO(user, false, false);
    }
}
