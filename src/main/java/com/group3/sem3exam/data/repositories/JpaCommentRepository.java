package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.CommentParent;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
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
    public JpaCommentRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Comment.class);
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
        return getEntityManager()
                .createQuery("SELECT c FROM Comment c WHERE c.parent = :parent")
                .setParameter("parent", parent)
                .getResultList();
    }

    @Override
    public Comment create(User author, String contents, CommentParent parent)
    {
        Comment comment = new Comment(contents, author, LocalDateTime.now(), parent);
        getEntityManager().persist(comment);
        return comment;
    }

    @Override
    public List<Comment> getPaginated(CommentParent parent, Integer pageSize, Integer pageNumber)
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        return getEntityManager()
                .createQuery("SELECT c FROM Comment c WHERE c.parent = :parent", Comment.class)
                .setParameter("parent", parent)
                .setFirstResult(pageSize * (pageNumber - 1))
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Returns the number of comments on the provided parent.
     *
     * @param commentParent The comment
     * @return The number of comments on the provided parent.
     */
    @Override
    public int count(CommentParent commentParent)
    {
        return (int) (long) getEntityManager()
                .createQuery("SELECT count(c) FROM Comment c WHERE c.parent = :parent", Long.class)
                .setParameter("parent", commentParent)
                .getSingleResult();
    }

    @Override
    public Integer delete(Comment comment)
    {
    return  getEntityManager().createQuery("DELETE FROM Comment c WHERE c.id = :id ")
            .setParameter("id", comment.getId())
            .executeUpdate();
    }
}
