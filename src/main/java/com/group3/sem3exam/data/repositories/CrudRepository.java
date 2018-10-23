package com.group3.sem3exam.data.repositories;

public interface CrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>> extends ReadCrudRepository<E, K>
{

    /**
     * Indicates to the repository that all write operations should be committed to the database immediately.
     *
     * @return this
     */
    void autoCommit();

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
