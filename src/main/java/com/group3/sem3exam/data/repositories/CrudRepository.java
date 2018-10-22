package com.group3.sem3exam.data.repositories;

public interface CrudRepository<E, ID> extends ReadCrudRepository<E, ID>
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
    E delete(ID id);
}
