package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer>
{


    Post createPost(User user, String title, String body, LocalDateTime time);

    List<Post> getByUserId(User user);
}
