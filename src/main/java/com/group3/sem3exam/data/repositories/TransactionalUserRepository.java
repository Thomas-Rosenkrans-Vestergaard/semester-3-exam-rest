package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.time.LocalDate;

public class TransactionalUserRepository extends TransactionalCrudRepository<User, Integer> implements UserRepository
{

    /**
     * Creates a new {@link TransactionalUserRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public TransactionalUserRepository(EntityManager entityManager)
    {
        super(entityManager, User.class);
    }

    /**
     * Creates a new {@link TransactionalUserRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public TransactionalUserRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, User.class);
    }

    /**
     * Creates a new {@link TransactionalUserRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalUserRepository(Transaction transaction)
    {
        super(transaction, User.class);
    }

    /**
     * Inserts a new user into the repository with the provided information. Note that the operation is not committed.
     *
     * @param name         The name of the new user.
     * @param email        The email of the new user.
     * @param passwordHash The password hash of the new user.
     * @param city         The city the new user resides in.
     * @param gender       The gender of the new user.
     * @param dateOfBirth  The date of birth of the new user.
     * @return The resulting new user.
     */
    @Override
    public User createUser(String name, String email, String passwordHash, City city, Gender gender, LocalDate dateOfBirth)
    {
        User user = new User(name, email, passwordHash, city, gender, dateOfBirth);
        getEntityManager().persist(user);
        return user;
    }

    /**
     * Returns the user with the provided email.
     *
     * @param email The email fo the user to find and return.
     * @return The user with the provided email, {@code null} when no such user exists.
     */
    @Override
    public User getByEmail(String email)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
