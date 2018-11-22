package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class JpaCommentRepository extends JpaCrudRepository<Comment, Integer> implements CommentRepository
{

    public JpaCommentRepository(EntityManager entityManager)
    {
        super(entityManager, Comment.class);
    }


    public JpaCommentRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Comment.class);
    }

    public JpaCommentRepository(JpaTransaction transaction)
    {
        super(transaction, Comment.class);
    }

    public List<Comment> getByPost(Integer postId)
    {
        return getEntityManager().createQuery("SELECT c FROM Comment c where c.post.id = :postId", Comment.class)
                                 .setParameter("postId", postId)
                                 .getResultList();
    }
}
