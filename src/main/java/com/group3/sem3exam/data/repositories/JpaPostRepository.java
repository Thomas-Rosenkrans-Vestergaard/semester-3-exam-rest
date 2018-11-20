package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
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
        return post;
    }

    @Override
    public List<Post> getByUserId(User author)
    {
      return getEntityManager().createQuery("SELECT Post FROM Post p where p.author = :author", Post.class)
                          .setParameter("author", author)
                          .getResultList();

    }

    @Override
    public List<Post> getTimeline(Integer userId)
    {
        return getEntityManager()
                .createQuery("SELECT p FROM Post p WHERE p.author IN " +
                             "(SELECT f.pk.friend FROM Friendship f WHERE f.pk.owner = :id) " +
                             "ORDER BY p.createdAt DESC", Post.class)
                .setParameter("id", userId)
                .getResultList()
                ;
    }

}
