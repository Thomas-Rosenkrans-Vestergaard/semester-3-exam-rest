package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Comment;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class JpaCommentRepository extends JpaReadCrudRepository<Comment, Integer> implements CommentRepository
{
    public JpaCommentRepository(EntityManager entityManager, Class<Comment> c)
    {
        super(entityManager, c);
    }

    public JpaCommentRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Comment.class);
    }

    public List<Comment> getCommentsByPost(Integer postId){
        return getEntityManager().createQuery("SELECT Comment FROM Comment c where c.post.id = :postId", Comment.class)
                                 .setParameter("postId", postId)
                                 .getResultList();
    }
}
