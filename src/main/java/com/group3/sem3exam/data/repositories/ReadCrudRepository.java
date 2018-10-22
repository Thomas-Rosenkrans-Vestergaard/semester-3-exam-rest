package com.group3.sem3exam.data.repositories;

import java.util.List;

public interface ReadCrudRepository<E, ID>
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
     * Checks whether or not an entity with the provided id exists.
     *
     * @param id The id to check for.
     * @return {@code true} when an entity with the provided id exists, {@code false} otherwise.
     */
    boolean exists(ID id);
}
