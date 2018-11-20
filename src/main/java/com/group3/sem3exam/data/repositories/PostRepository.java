package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository
{


    Post createPost(User user, String title, String body, LocalDateTime time);

    Post getPost(Integer post);

    List<Post> getPostsByUser(User user);

    List<Post> getTimeline(Integer user);
}
