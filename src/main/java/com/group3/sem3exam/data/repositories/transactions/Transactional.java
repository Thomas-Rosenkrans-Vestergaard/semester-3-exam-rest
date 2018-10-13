package com.group3.sem3exam.data.repositories.transactions;

public interface Transactional extends AutoCloseable
{

    /**
     * Begins the transaction.
     *
     * @return this
     */
    void begin();

    /**
     * Commits the current transaction.
     *
     * @return this
     */
    void commit();

    /**
     * Rolls back the current transaction.
     *
     * @return this
     */
    void rollback();

    /**
     * Closes the transaction.
     */
    void close();
}
