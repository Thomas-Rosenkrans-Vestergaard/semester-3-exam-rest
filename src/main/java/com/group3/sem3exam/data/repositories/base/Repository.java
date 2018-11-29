package com.group3.sem3exam.data.repositories.base;

import com.group3.sem3exam.data.repositories.transactions.Transactional;

/**
 * Represents a repository of some type of entity.
 */
public interface Repository extends AutoCloseable, Transactional
{

    /**
     * Closes the resource.
     */
    @Override
    void close();
}
