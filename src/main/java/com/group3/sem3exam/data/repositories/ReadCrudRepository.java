package com.group3.sem3exam.data.repositories;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface defining common read operations on some {@code RepositoryEntity}.
 *
 * @param <E> The type of the entity managed by the repository.
 * @param <K> The type of the key of the entities managed by the repository.
 */
public interface ReadCrudRepository<E extends RepositoryEntity<K>, K extends Comparable<K>> extends Repository
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
     *                   return The images on the selected page.
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
     * @return The entity with the provided id, or {@code null} when no such entity exists.
     */
    E get(K id);

    /**
     * Returns all the entities with the provided ids. When an entity with a provided id does not exist, the
     * {@code null} value is not inserted in the return map.
     *
     * @param ids The ids of entity to return.
     * @return The returned entities mapped to their id.
     */
    Map<K, E> get(Set<K> ids);

    /**
     * Checks whether or not an entity with the provided id exists.
     *
     * @param id The id to check for.
     * @return {@code true} when an entity with the provided id exists, {@code false} otherwise.
     */
    boolean exists(K id);
}
