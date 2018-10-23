package com.group3.sem3exam.data.repositories;

public interface Repository extends AutoCloseable
{

    /**
     * Closes the resource
     */
    @Override
    void close();

    /**
     * Indicates to the repository that all write operations should be committed to the database immediately.
     *
     * @return this
     */
    void autoCommit();
}
