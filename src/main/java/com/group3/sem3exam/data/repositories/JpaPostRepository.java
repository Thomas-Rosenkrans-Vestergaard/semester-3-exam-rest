package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JpaPostRepository extends JpaCrudRepository<Post, Integer> implements PostRepository
{

    public JpaPostRepository(EntityManager entityManager)
    {
        super(entityManager, Post.class);
    }



    public JpaPostRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Post.class);
    }

    public JpaPostRepository(JpaTransaction transaction)
    {
        super(transaction, Post.class);
    }

    @Override
    public Post createPost(User user, String title, String body, LocalDateTime createdAt)
    {
        Post post = new Post(body, title, user, createdAt);
        getEntityManager().persist(post);
        close();
        return post;
    }

    @Override
    public Post getPost(Integer post)
    {
        return getEntityManager()
                .createQuery("SELECT p from Post p WHERE p.id = :id", Post.class)
                .setParameter("id", post)
                .getSingleResult()
                ;
    }

    @Override
    public List<Post> getPostsByUser(User user)
    {
        return null;
    }
}
