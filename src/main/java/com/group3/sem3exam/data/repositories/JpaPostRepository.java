package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JpaPostRepository extends JpaCrudRepository<Post, Integer> implements PostRepository
{

    /**
     * Creates a new {@link JpaPostRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaPostRepository(EntityManager entityManager)
    {
        super(entityManager, Post.class);
    }

    /**
     * Creates a new {@link JpaPostRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaPostRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Post.class);
    }

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaPostRepository(JpaTransaction transaction)
    {
        super(transaction, Post.class);
    }

    @Override
    public Post create(User user, String body, List<Image> images, LocalDateTime time)
    {
        Post post = new Post(body, images, user, time);
        getEntityManager().persist(post);
        return post;
    }

    @Override
    public List<Post> getByAuthor(User author)
    {
        return getEntityManager().createQuery("SELECT p FROM Post p where p.author = :author", Post.class)
                                 .setParameter("author", author)
                                 .getResultList();
    }

    @Override
    public List<Post> getTimeline(User user, Integer pageSize, Integer cutoff)
    {
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT p FROM Post p WHERE p.id < :cutoff AND p.author IN " +
                                         "(SELECT f.pk.friend FROM Friendship f WHERE f.pk.owner = :user) " +
                                         "ORDER BY p.id DESC", Post.class);
            query.setParameter("user", user);
            query.setParameter("cutoff", cutoff == null ? Integer.MAX_VALUE : cutoff);
            query.setMaxResults(pageSize);
            List<Post> posts = query.getResultList();
            return posts;
        } catch (Exception e) {
            return new ArrayList<>(); // TODO: fix SQLGrammarException, maybe caused by not having friends
        }
    }

    @Override
    public List<Post> getByAuthorRolling(User user, Integer pageSize, Integer last)
    {
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT p FROM Post p WHERE p.id < :last AND p.author = :user " +
                                         "ORDER BY p.id DESC", Post.class);
            query.setParameter("user", user);
            query.setParameter("last", last == null ? Integer.MAX_VALUE : last);
            query.setMaxResults(pageSize);
            List<Post> posts = query.getResultList();
            return posts;
        } catch (Exception e) {
            return new ArrayList<>(); // TODO: fix SQLGrammarException, maybe caused by not having friends
        }
    }
}
