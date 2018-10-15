package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.time.LocalDate;

public class TransactionalUserRepository extends TransactionalCrudRepository<User, Integer> implements UserRepository
{

    public TransactionalUserRepository(EntityManager entityManager)
    {
        super(entityManager, User.class);
    }

    public TransactionalUserRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, User.class);

    }

    public TransactionalUserRepository()
    {
        super(User.class);
    }

    @Override
    public User createUser(String name, String email, String passwordHash, City city, Gender gender, LocalDate dateOfBirth)
    {
        User user = new User(name, email, passwordHash, city, gender, dateOfBirth);
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(user);
        getEntityManager().getTransaction().commit();
        return user;
    }



    @Override
    public User getByEmail(String email)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
