package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.CommentParent;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

public class JpaCommentRepository extends JpaCrudRepository<Comment, Integer> implements CommentRepository
{

    /**
     * Creates a new {@link JpaCommentRepository} using the provided entity manager.
     *
     * @param entityManager The entity manager to perform operations upon.
     */
    public JpaCommentRepository(EntityManager entityManager)
    {
        super(entityManager, Comment.class);
    }

    /**
     * Creates a new {@link JpaCommentRepository} using the provided entity manager factory.
     *
     * @param entityManagerFactory The entity manager factory used to create the entity manager to perform operations
     *                             upon.
     */
    public JpaCommentRepository(EntityManagerFactory entityManagerFactory, Class<Comment> c)
    {
        super(entityManagerFactory, c);
    }

    /**
     * Creates a new {@link JpaCommentRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaCommentRepository(JpaTransaction transaction)
    {
        super(transaction, Comment.class);
    }

    @Override
    public CommentParent getParent(Integer id)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT cp FROM CommentParent cp WHERE cp.id = :parent", CommentParent.class)
                    .setParameter("parent", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean parentExists(Integer parent)
    {
        return getEntityManager()
                       .createQuery("SELECT count(p) FROM CommentParent p WHERE p.id = :parent")
                       .setParameter("parent", parent)
                       .getFirstResult() > 0;
    }

    @Override
    public List<Comment> getAll(CommentParent parent)
    {
        return parent.getComments();
    }

    @Override
    public Comment create(User author, String contents, CommentParent parent)
    {
        Comment comment = new Comment(contents, author, LocalDateTime.now(), parent);
        getEntityManager().persist(comment);
        return comment;
    }
}
