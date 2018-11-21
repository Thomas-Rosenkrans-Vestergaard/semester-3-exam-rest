package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
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
    public List<Post> getTimelinePosts(Integer userId, Integer pageSize, Integer cutoff)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p FROM Post p WHERE p.id < :cutoff AND p.author IN " +
                                     "(SELECT f.pk.friend FROM Friendship f WHERE f.pk.owner.id = :userId) " +
                                     "ORDER BY p.id DESC", Post.class);
        query.setParameter("userId", userId);
        query.setParameter("cutoff", cutoff == null ? Integer.MAX_VALUE : cutoff);
        query.setMaxResults(pageSize);
        List<Post> posts = query.getResultList();
        return posts;
    }
}
