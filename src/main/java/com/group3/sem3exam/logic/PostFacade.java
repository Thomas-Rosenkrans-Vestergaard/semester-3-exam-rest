package com.group3.sem3exam.logic;


import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.PostRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class PostFacade<T extends Transaction>
{

    private final Supplier<T>                 transactionFactory;
    private final Function<T, PostRepository> postRepositoryFactory;
    private final Function<T, UserRepository> userRepositoryFactory;

    public PostFacade(Supplier<T> transactionFactory, Function<T, PostRepository> postRepositoryFactory, Function<T, UserRepository> userRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.postRepositoryFactory = postRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;
    }


    public Post createPost(String title, String body, User author, LocalDateTime time)
    {

        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PostRepository pr   = postRepositoryFactory.apply(transaction);
            Post           post = pr.createPost(author, title, body, time);

            transaction.commit();
            return post;

        }
    }


    public Post get(Integer id) throws ResourceNotFoundException
    {
        PostRepository pr = postRepositoryFactory.apply(transactionFactory.get());

        Post post = pr.get(id);

        if (post == null) {
            throw new ResourceNotFoundException(Post.class, id, 404);
        }
        return post;
    }

    public List<Post> getTimeline(Integer id) throws ResourceNotFoundException
    {
        PostRepository pr   = postRepositoryFactory.apply(transactionFactory.get());
        List<Post>     post = pr.getTimeline(id);
        if (post == null) {
            throw new ResourceNotFoundException(Post.class, id, 404);
        }
        return post;
    }

    public List<Post> getPostByUser(Integer id) throws ResourceNotFoundException
    {
        UserRepository ur    = userRepositoryFactory.apply(transactionFactory.get());
        User           user  = ur.get(id);
        PostRepository pr    = postRepositoryFactory.apply(transactionFactory.get());
        List<Post>     posts = pr.getByUserId(user);
        if (user == null) {
            throw new ResourceNotFoundException(Post.class, user.getId(), 422);
        }
        return posts;
    }
}

