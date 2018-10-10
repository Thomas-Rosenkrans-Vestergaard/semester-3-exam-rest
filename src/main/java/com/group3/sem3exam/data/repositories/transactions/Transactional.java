package com.group3.sem3exam.data.repositories.transactions;

public interface Transactional
{

    /**
     * Begins the transaction.
     *
     * @return this
     */
    Transactional begin();

    /**
     * Commits the current transaction.
     *
     * @return this
     */
    Transactional commit();

    /**
     * Rolls back the current transaction.
     *
     * @return this
     */
    Transactional rollback();

    /**
     * Closes the transaction.
     *
     * @return this
     */
    Transactional close();
}
