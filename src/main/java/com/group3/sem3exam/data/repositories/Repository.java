package com.group3.sem3exam.data.repositories;

public interface Repository extends AutoCloseable
{

    /**
     * Closes the resource
     */
    @Override
    void close();
}
