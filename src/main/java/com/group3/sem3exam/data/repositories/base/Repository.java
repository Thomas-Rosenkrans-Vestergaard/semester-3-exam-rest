package com.group3.sem3exam.data.repositories.base;

/**
 * Represents a repository of some type of entity.
 */
public interface Repository extends AutoCloseable
{

    /**
     * Closes the resource.
     */
    @Override
    void close();
}