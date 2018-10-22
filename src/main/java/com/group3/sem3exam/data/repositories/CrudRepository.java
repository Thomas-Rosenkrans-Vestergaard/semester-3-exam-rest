package com.group3.sem3exam.data.repositories;

import java.util.List;

public interface CrudRepository<E, ID>
{

    /**
     * Returns all the entities in the repository.
     *
     * @return All the entities in the repository.
     */
    List<E> get();

    /**
     * Returns a *page* of entities in the repository. The entities retrieved, in a zero-based manor, are defined
     * from <code> (pageNumber - 1) * pageSize</code> to <code>(pageSize) * perPage</code>.
     *
     * @param pageSize   The number of entities per page.
     * @param pageNumber The page number to getCities. Starts at 1.
     * @return
     */
    List<E> getPaginated(int pageSize, int pageNumber);

    /**
     * Returns the number of defined entities.
     *
     * @return The number of defined entities.
     */
    long count();

    /**
     * Returns the entity with the provided id.
     *
     * @param id The id of the entity to return.
     * @return The entity with the provided id, or {@code null} when no such entity exsits.
     */
    E get(ID id);

    /**
     * Persists the provided entity.
     *
     * @param entity The entity to persist.
     * @return The updated entity.
     */
    E persist(E entity);

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
