package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * An implementation of the {@code ImageRepository} interface, backed by a JPA data source.
 */
public class JpaImageRepository extends JpaCrudRepository<Image, Integer> implements ImageRepository
{
    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaImageRepository(EntityManager entityManager)
    {
        super(entityManager, Integer.class, "id", Image.class);
    }

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaImageRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Integer.class, "id", Image.class);
    }

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaImageRepository(JpaTransaction transaction)
    {
        super(transaction, Integer.class, "id", Image.class);
    }

    @Override
    public Image create(String title, String uri, User user)
    {
        Image image = new Image(title, uri, user);
        getEntityManager().persist(image);
        return image;
    }

    @Override
    public List<Image> getByUser(Integer user)
    {
        return getEntityManager()
                .createQuery("SELECT i FROM Image i WHERE i.user.id = :user", Image.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Image> getByUserPaginated(Integer user, int pageSize, int pageNumber)
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        return getEntityManager()
                .createQuery("SELECT i FROM Image i WHERE i.user.id = :user", Image.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .setParameter("user", user)
                .getResultList();
    }
}