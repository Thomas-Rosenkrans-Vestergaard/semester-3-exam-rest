package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.transactions.TransactionalRepository;

/**
 * An interface defining common read and write operations on some {@code RepositoryEntity}.
 *
 * @param <E> The type of the entity managed by the repository.
 * @param <K> The type of the key of the entities managed by the repository.
 */
public interface CrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>>
        extends ReadCrudRepository<E, K>,
                TransactionalRepository
{

    /**
     * Forces the entity to update.
     *
     * @param entity The entity to update.
     * @return The updated entity.
     */
    E update(E entity);

    /**
     * Deletes the entity with the provided id.
     *
     * @param id The id of the entity to delete.
     * @return The deleted entity, or {@code null} when no entity was deleted.
     */
    E delete(K id);
}
