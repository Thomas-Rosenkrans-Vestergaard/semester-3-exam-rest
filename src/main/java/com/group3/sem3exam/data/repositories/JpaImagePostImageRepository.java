package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.ImagePostImage;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaImagePostImageRepository extends JpaCrudRepository<ImagePostImage, Integer> implements ImagePostImageRepository
{
    public JpaImagePostImageRepository(EntityManager entityManager)
    {
        super(entityManager, ImagePostImage.class);
    }
    public JpaImagePostImageRepository(EntityManagerFactory entityManagerFactory)
    {
         super(entityManagerFactory, ImagePostImage.class);
    }

    public JpaImagePostImageRepository(JpaTransaction transaction)
    {
        super(transaction, ImagePostImage.class);
    }

    @Override
    public ImagePostImage create(String data, User user, String thumbnail)
    {
        ImagePostImage imagePostImage = new ImagePostImage(data, thumbnail, user);
        getEntityManager().persist(imagePostImage);
        return imagePostImage;

    }
}
