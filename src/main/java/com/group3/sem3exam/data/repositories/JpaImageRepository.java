package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

public class JpaImageRepository extends JpaCrudRepository<Image, Integer> implements ImageRepository
{
    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaImageRepository(EntityManager entityManager)
    {
        super(entityManager, Image.class);
    }

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaImageRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Image.class);
    }

    /**
     * Creates a new {@link JpaCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */

    public JpaImageRepository(JpaTransaction transaction)
    {
        super(transaction, Image.class);
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
                .createQuery("SELECT i FROM Image i WHERE Image.user.id = :user", Image.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Image> getByUserPaginated(Integer user, int pageSize, int pageNumber)
    {
        return null;
    }


    /**
     * Returns the cities in the region with the provided id.
     *
     * @param region The id of the region to return the cities of.
     * @return The cities in the region with the provided id.
     */


}