package com.group3.sem3exam.logic;


import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.PostRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

public class PostFacade<T extends Transaction>
{

    private final Supplier <T> transactionFactory;
    private final Function<T, PostRepository> postRepositoryFactory;

    public PostFacade(Supplier<T> transactionFactory, Function<T, PostRepository> postRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.postRepositoryFactory = postRepositoryFactory;
    }


    public Post createPost(String title, String body, User author, LocalDateTime time) throws ResourceNotFoundException
    {

        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PostRepository pr   = postRepositoryFactory.apply(transaction);
            Post           post = pr.createPost(author, title, body, time);

            if (post == null) {
                throw new ResourceNotFoundException(Post.class, post.getId(), 422);
            }
            transaction.commit();
            return post;

        }
    }


    public Post get(Integer id) throws ResourceNotFoundException
    {
            PostRepository pr = postRepositoryFactory.apply(transactionFactory.get());
            Post post = pr.getPost(id);
            if(post == null){
                throw new ResourceNotFoundException(Post.class, id, 404);
            }
            return post;
    }
    }

