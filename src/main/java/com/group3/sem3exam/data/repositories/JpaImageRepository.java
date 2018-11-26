package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.GalleryImage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * An implementation of the {@code ImageRepository} interface, backed by a JPA data source.
 */
public class JpaImageRepository extends JpaCrudRepository<GalleryImage, Integer> implements ImageRepository
{

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaImageRepository(EntityManager entityManager)
    {
        super(entityManager, GalleryImage.class);
    }

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaImageRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, GalleryImage.class);
    }

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaImageRepository(JpaTransaction transaction)
    {
        super(transaction, GalleryImage.class);
    }

    @Override
    public GalleryImage create(String description, String full, String thumbnail, User user)
    {
        GalleryImage image = new GalleryImage(description, full, thumbnail, user);
        getEntityManager().persist(image);
        return image;
    }

    @Override
    public List<GalleryImage> getByUser(Integer user)
    {
        return getEntityManager()
                .createQuery("SELECT i FROM GalleryImage i WHERE i.user.id = :user", GalleryImage.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<GalleryImage> getByUserPaginated(Integer user, int pageSize, int pageNumber)
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        return getEntityManager()
                .createQuery("SELECT i FROM GalleryImage i WHERE i.user.id = :user", GalleryImage.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public int countByUser(User user)
    {
        return user.getImages().size();
    }
}