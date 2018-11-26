package com.group3.sem3exam.data.repositories.base;

/**
 * Represents an entity contained in a {@code Repository}.
 *
 * @param <K> The type of the primary key of the entity. The key must be unique for any {@code RepositoryEntity}.
 */
public interface RepositoryEntity<K extends Comparable<K>>
{

    /**
     * Returns the primary key of the entity.
     *
     * @return The primary key of the entity.
     */
    K getId();
}
