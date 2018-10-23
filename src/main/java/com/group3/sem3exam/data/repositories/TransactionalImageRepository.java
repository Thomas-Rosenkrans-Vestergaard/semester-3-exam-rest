package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.AbstractTransactionalRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

public class TransactionalImageRepository extends TransactionalCrudRepository<Image, Integer> implements ImageRepository
{


    public TransactionalImageRepository(EntityManager entityManager)
    {
        super(entityManager, Image.class);
    }

    /**
     * Creates a new {@link TransactionalRegionRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public TransactionalImageRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Image.class);
    }

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalImageRepository(Transaction transaction)
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
    public List<Image> getByUser(Integer user){
            return getEntityManager()
                    .createQuery("SELECT i FROM Image i WHERE i.user.id = :user", Image.class)
                    .setParameter("user", user)
                    .getResultList();

        }


    @Override
    public List<Image> getByUserPaginated(Integer user, int pageSize, int pageNumber)
    {

    }
}