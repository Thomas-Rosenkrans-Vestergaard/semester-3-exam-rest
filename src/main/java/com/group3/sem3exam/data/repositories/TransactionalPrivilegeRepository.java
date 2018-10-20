package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Privilege;
import com.group3.sem3exam.data.entities.PrivilegeMapping;
import com.group3.sem3exam.data.entities.ThirdParty;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.Set;

public class TransactionalPrivilegeRepository extends TransactionalCrudRepository<PrivilegeMapping, Integer>
        implements PrivilegeRepository
{

    /**
     * Creates a new {@link TransactionalPrivilegeRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public TransactionalPrivilegeRepository(EntityManager entityManager)
    {
        super(entityManager, PrivilegeMapping.class);
    }

    /**
     * Creates a new {@link TransactionalPrivilegeRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public TransactionalPrivilegeRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, PrivilegeMapping.class);
    }

    /**
     * Creates a new {@link TransactionalPrivilegeRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public TransactionalPrivilegeRepository(Transaction transaction)
    {
        super(transaction, PrivilegeMapping.class);
    }

    /**
     * Checks whether or not the third party can perform the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privilege  The privilege to check for.
     * @return {@code true} when the third party can perform the provided privilege on behalf of the provided user.
     */
    @Override
    public boolean can(ThirdParty thirdParty, User user, Privilege privilege)
    {
        long count = getEntityManager()
                .createQuery("SELECT count(*) FROM PrivilegeMapping pm " +
                             "WHERE pm.thirdParty = :thirdParty AND pm.user = :user AND pm.privilege = :privilege", Long.class)
                .setParameter("thirdParty", thirdParty)
                .setParameter("user", user)
                .setParameter("privilege", privilege)
                .getSingleResult();

        return count > 0;
    }

    /**
     * Checks whether or not the third party can perform all of the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privileges The privileges to check for.
     * @return {@code true} when the third party can perform all of the provided privilege on behalf of the provided
     * user.
     */
    @Override
    public boolean can(ThirdParty thirdParty, User user, Set<Privilege> privileges)
    {
        return get(thirdParty, user).containsAll(privileges);
    }

    /**
     * Allows the provided third party to perform the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privilege  The privilege to allow the third party to perform.
     */
    @Override
    public void allow(ThirdParty thirdParty, User user, Privilege privilege)
    {
        if (can(thirdParty, user, privilege))
            return;

        getEntityManager().persist(new PrivilegeMapping(user, thirdParty, privilege));
    }

    /**
     * Allows the provided third party to perform the provided privileges on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privileges The privileges to allow the third party to perform.
     */
    @Override
    public void allow(ThirdParty thirdParty, User user, Set<Privilege> privileges)
    {
        Set<Privilege> currentPrivileges = get(thirdParty, user);
        privileges.removeAll(currentPrivileges);

        for (Privilege privilege : privileges)
            allow(thirdParty, user, privilege);
    }

    /**
     * Denies the provided third party to perform the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privilege  The privilege to deny the third party.
     */
    @Override
    public void deny(ThirdParty thirdParty, User user, Privilege privilege)
    {
        getEntityManager()
                .createQuery("DELETE FROM PrivilegeMapping pm " +
                             "WHERE pm.thirdParty = :tp AND pm.user = :user AND pm.privilege = :privilege")
                .setParameter("tp", thirdParty)
                .setParameter("user", user)
                .setParameter("privilege", privilege)
                .executeUpdate();
    }

    /**
     * Denies the provided third party to perform the provided privileges on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privileges The privileges to deny the third party.
     */
    @Override
    public void deny(ThirdParty thirdParty, User user, Set<Privilege> privileges)
    {
        Set<Privilege> currentPrivileges = get(thirdParty, user);
        currentPrivileges.retainAll(privileges);

        for (Privilege privilege : currentPrivileges)
            deny(thirdParty, user, privilege);
    }

    /**
     * Returns all the privileges the provided third party has when representing the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @return All the privileges the provided third party has when representing the provided user.
     */
    @Override
    public Set<Privilege> get(ThirdParty thirdParty, User user)
    {
        return new HashSet<>(
                getEntityManager()
                        .createQuery("SELECT pm.privilege FROM PrivilegeMapping pm " +
                                     "WHERE pm.thirdParty.id = :tp AND pm.user.id = :user", Privilege.class)
                        .setParameter("tp", thirdParty.getId())
                        .setParameter("user", user.getId())
                        .getResultList());
    }
}
