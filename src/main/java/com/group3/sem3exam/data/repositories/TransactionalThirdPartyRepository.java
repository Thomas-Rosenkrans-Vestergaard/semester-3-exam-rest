package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.ThirdParty;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class TransactionalThirdPartyRepository extends TransactionalCrudRepository<ThirdParty, Integer>
        implements ThirdPartyRepository
{

    /**
     * Creates a new {@link TransactionalThirdPartyRepository} using the provided entity manager.
     *
     * @param entityManager The entity manager to perform operations upon.
     */
    public TransactionalThirdPartyRepository(EntityManager entityManager)
    {
        super(entityManager, ThirdParty.class);
    }

    /**
     * Creates a new {@link TransactionalThirdPartyRepository} using the provided entity manager factory.
     *
     * @param entityManagerFactory The entity manager factory used to create the entity manager to perform operations
     *                             upon.
     */
    public TransactionalThirdPartyRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, ThirdParty.class);
    }

    /**
     * Creates a new {@link TransactionalThirdPartyRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalThirdPartyRepository(Transaction transaction)
    {
        super(transaction, ThirdParty.class);
    }

    /**
     * Creates a new third party entity using the provided information.
     *
     * @param name         The name of the new third party.
     * @param passwordHash The password hash of the new third party.
     * @return The newly created third party entity.
     */
    @Override
    public ThirdParty create(String name, String passwordHash)
    {
        ThirdParty thirdParty = new ThirdParty(name, passwordHash);
        getEntityManager().persist(thirdParty);
        return thirdParty;
    }

    /**
     * Returns the third party with the provided name.
     *
     * @param name The name of the third party to return.
     * @return The third party with the provided name.
     */
    @Override
    public ThirdParty getByName(String name)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT tp FROM ThirdParty tp WHERE tp.name = :name", ThirdParty.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
