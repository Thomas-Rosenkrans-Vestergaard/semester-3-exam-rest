package com.group3.sem3exam.data.repositories.transactions;

import com.group3.sem3exam.data.repositories.Repository;

import javax.persistence.EntityManager;

public interface TransactionalRepository extends Transactional, Repository
{

    /**
     * Returns the currently active entity manager instance.
     *
     * @return The currently active entity manager.
     */
    EntityManager getEntityManager();

    /**
     * Sets the currently active entity manager instance.
     *
     * @param entityManger The new active manager.
     */
    void setEntityManager(EntityManager entityManger);
}
