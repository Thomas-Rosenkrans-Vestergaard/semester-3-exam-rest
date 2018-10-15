package com.group3.sem3exam.data.repositories.transactions;

public interface Transactional extends AutoCloseable
{

    /**
     * Begins the transaction.
     */
    void begin();

    /**
     * Commits the current transaction.
     */
    void commit();

    /**
     * Rolls back the current transaction.
     */
    void rollback();

    /**
     * Closes the transaction.
     */
    void close();
}
