package com.group3.sem3exam.data.repositories;

public interface RepositoryEntity<K extends Comparable<K>>
{

    /**
     * Returns the primary key of the entity.
     *
     * @return The primary key of the entity.
     */
    K getId();
}
