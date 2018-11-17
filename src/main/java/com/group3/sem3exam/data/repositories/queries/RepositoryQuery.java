package com.group3.sem3exam.data.repositories.queries;

import com.group3.sem3exam.data.repositories.RepositoryEntity;
import com.group3.sem3exam.data.repositories.queries.tree.Direction;

import java.util.List;

/**
 * Represents a query on a repository.
 * <p>
 * Any operation not supported by an implementation throws an {@link UnsupportedOperationException}.
 *
 * @param <K> The key of the type of the entity handled by this query.
 * @param <E> The type of the entity handled by this query.
 */
public interface RepositoryQuery<K extends Comparable<K>, E extends RepositoryEntity<K>>
{

    /**
     * Adds an {@code equals} constraint to the query. The value of the provided {@code attribute} must be equal to the
     * provided {@code object}.
     *
     * @param attribute The attribute value that must be equal to the object.
     * @param object    The object the attribute must equal.
     * @return this
     * @see RepositoryQuery#whereNot(String attribute, Object object) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereEq(String attribute, Object object);

    /**
     * Adds a {@code not equals} constraint to the query. The value of the provided {@code attribute} must not be equal to the
     * provided {@code object}.
     *
     * @param attribute The attribute value that must not be equal to the object.
     * @param object    The object the attribute must not equal.
     * @return this
     * @see RepositoryQuery#whereEq(String, Object) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereNot(String attribute, Object object);

    /**
     * Adds an {@code in} constraint to the query. The value of the provided {@code attribute} must be present in the
     * provided list of {@code objects}.
     *
     * @param attribute The attribute value that must be present in the provided {@code objects}.
     * @param objects   The objects that the value of the attribute must match.
     * @return this
     * @see RepositoryQuery#whereNotIn(String, Object...) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereIn(String attribute, Object... objects);

    /**
     * Adds an {@code in} constraint to the query. The value of the provided {@code attribute} must be present in the
     * provided list of {@code objects}.
     *
     * @param attribute The attribute value that must be present in the provided {@code objects}.
     * @param objects   The objects that the value of the attribute must match.
     * @return this
     * @see RepositoryQuery#whereNotIn(String, List) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereIn(String attribute, List<Object> objects);

    /**
     * Adds a {@code not in} constraint to the query. The value of the provided {@code attribute} must not be present in
     * the provided list of {@code objects}.
     *
     * @param attribute The attribute value that must not be present in the provided {@code objects}.
     * @param objects   The objects that the value of the attribute must not match.
     * @return this
     * @see RepositoryQuery#whereIn(String, Object...) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereNotIn(String attribute, Object... objects);

    /**
     * Adds a {@code not in} constraint to the query. The value of the provided {@code attribute} must not be present in
     * the provided list of {@code objects}.
     *
     * @param attribute The attribute value that must not be present in the provided {@code objects}.
     * @param objects   The objects that the value of the attribute must not match.
     * @return this
     * @see RepositoryQuery#whereIn(String, Object...) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereNotIn(String attribute, List<Object> objects);

    /**
     * Adds a {@code between} constraint to the query. The value of the provided {@code attribute} must be between
     * {@code start} and {@code end}. Whether or not the operation is inclusive is implementation specific.
     *
     * @param attribute The value of the attribute that must be between the provided {@code start} and {@code end}.
     * @param start     The start of the between constraint.
     * @param end       The end of the between constraint.
     * @return this
     * @see RepositoryQuery#whereOutside(String, Object, Object) The inverse operation.
     */
    RepositoryQuery<K, E> whereBetween(String attribute, Object start, Object end);

    /**
     * Adds a {@code not between} constraint to the query. The value of the provided {@code attribute} must not be between
     * {@code start} and {@code end}. Whether or not the operation is inclusive is implementation specific.
     *
     * @param attribute The value of the attribute that must not be between the provided {@code start} and {@code end}.
     * @param start     The start of the between constraint.
     * @param end       The end of the between constraint.
     * @return this
     * @see RepositoryQuery#whereBetween(String, Object, Object) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereOutside(String attribute, Object start, Object end);

    /**
     * Adds a {@code like} constraint to the query. The value of the provided {@code attribute} must be similar to the
     * provided {@code object}. The behavior of this constraint is implementation specific.
     *
     * @param attribute The value of the attribute that must be {@code like} the provided {@code object}.
     * @param object    The value that the attribute value must be {@code like}
     * @return this
     * @see RepositoryQuery#whereNotLike(String, Object) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereLike(String attribute, Object object);

    /**
     * Adds a {@code like} constraint to the query. The value of the provided {@code attribute} must not be similar to
     * the provided {@code object}. The behavior of this constraint is implementation specific.
     *
     * @param attribute The value of the attribute that must not be {@code like} the provided {@code object}.
     * @param object    The value that the attribute value not must be {@code like}.
     * @return this
     * @see RepositoryQuery#whereLike(String, Object) The inverse of this operation.
     */
    RepositoryQuery<K, E> whereNotLike(String attribute, Object object);

    /**
     * Orders the provided {@code attribute} in the provided {@code direction}.
     *
     * @param attribute The attribute to order by.
     * @param direction The direction the attribute should be ordered in.
     * @return this
     * @see RepositoryQuery#desc(String) A cleaner way to order by DESC.
     * @see RepositoryQuery#asc(String) A cleaner way to order by ASC.
     */
    RepositoryQuery<K, E> order(String attribute, Direction direction);

    /**
     * Orders the provided {@code attribute} to descending.
     *
     * @param attribute The attribute to order by.
     * @return this
     */
    default RepositoryQuery<K, E> desc(String attribute)
    {
        return order(attribute, Direction.DESC);
    }

    /**
     * Orders the provided {@code attribute} to ascending.
     *
     * @param attribute The attribute to order by.
     * @return this
     */
    default RepositoryQuery<K, E> asc(String attribute)
    {
        return order(attribute, Direction.ASC);
    }

    /**
     * Sets the number of results to skip. Replaces the previously declared value. The default number of results
     * to skip is {@code 0}.
     *
     * @param n The number of queries to skip.
     * @return this
     */
    RepositoryQuery<K, E> skip(int n);

    /**
     * Sets the max number of results to return. Replaces the previously declared value. The default number of results
     * to skip is {@code Integer.MAX_VALUE}.
     *
     * @param n The maximum number of results to return.
     * @return this
     */
    RepositoryQuery<K, E> limit(int n);

    /**
     * Executes the query, returning all results.
     *
     * @return All the results in the query.
     */
    List<E> get();

    /**
     * Executes the query returning the result of a single page. The operation does not throw an exception when the
     * results are out of bounds. An empty list is instead returned.
     *
     * @param pageSize   The number of results on a single page. Where {@code pageSize >= 1}.
     * @param pageNumber The page to return the results from. Where {@code pageNumber >= 1}.
     * @return The results in the page.
     */
    List<E> getPage(int pageSize, int pageNumber);

    /**
     * Returns the first {@code n} result of the query. The operation does not throw an exception when the
     * results are out of bounds.
     *
     * @param n The number of results to retrieve.
     * @return The first {@code n} results of the query.
     */
    List<E> getFirst(int n);

    /**
     * Returns the {@code n}th result of the query. The operation does not throw an exception when the
     * results are out of bounds.
     *
     * @param n The index of the element to return. {@code n} starts at 0.
     * @return The {@code n}th result of the query. {@code null} when the result is out of bounds.
     */
    E getAt(int n);

    /**
     * Returns the first result of the query.
     *
     * @return The first result of the query. {@code null} when the result set is empty.
     */
    default E getFirst()
    {
        return getAt(0);
    }

    /**
     * Counts the number of results using the provided constraints. The declared {@code limit} does not
     * effect the result of this method.
     *
     * @return The number of results using the provided constraints.
     */
    long count();

    /**
     * sChecks that there exists a result when using the provided constraints. The declared {@code limit} does not
     * effect the result of this method.
     *
     * @return {@code true} when there exists at least one result when using the provided constraints.
     */
    boolean exists();

    /**
     * Checks if the given key exists in the list of results.
     *
     * @param key The key to check for existence of.
     * @return {@code true} when the given key exists in the list of results. {@code false} otherwise.
     */
    boolean contains(K key);

    /**
     * Returns the maximum value of the provided attribute.
     *
     * @param attribute The attribute to return the maximum value of.
     * @param vClass    The class of the return type.
     * @param <V>       The type of the return type.
     * @return The maximum value of the provided attribute, {@code null} when no result exists.
     */
    <V> V max(String attribute, Class<V> vClass);

    /**
     * Returns the minimum value of the provided attribute.
     *
     * @param attribute The attribute to return the minimum value of.
     * @param vClass    The class of the return type.
     * @param <V>       The type of the return type.
     * @return The minimum value of the provided attribute, {@code null} when no result exists.
     */
    <V> V min(String attribute, Class<V> vClass);

    /**
     * Returns the values of the provided attribute in the list of results.
     *
     * @param attribute The name of the attribute to return the values of.
     * @param vClass    The class of the return type.
     * @param <V>       The type of the return type.
     * @return The values of the provided attribute in the list of results.
     */
    <V> List<V> getAttributes(String attribute, Class<V> vClass);

    /**
     * Returns the keys of the list of results.
     *
     * @return The keys of the list of results.
     */
    List<K> getKeys();

    /**
     * Chunks the results in the query, providing each chunk to the provided callback.
     *
     * @param chunkSize The number of results in each chunk.
     * @param chunker   The The handler that the chunks are provided to.
     * @return {@code true} when the chunker was not stopped prematurely, and therefor ran through all the results
     * of the query, {@code false} otherwise.
     */
    boolean chunk(int chunkSize, Chunker<E> chunker);

    @FunctionalInterface
    interface Chunker<E>
    {

        /**
         * Handles a single chunk.
         *
         * @param chunk   The chunk provided to the handler.
         * @param stopper A runnable instance that can be called to stop the chunking of the results.
         */
        void handle(Chunk<E> chunk, Runnable stopper);
    }

    /**
     * Represents a chunk of results provided to the {@link Chunker} interface.
     *
     * @param <E> The type of the entity in the {@link Chunk}.
     */
    interface Chunk<E>
    {

        /**
         * Returns the index of the current chunk, starts at {@code 0}.
         *
         * @return The index of the current chunk, starts at {@code 0}.
         */
        int index();

        /**
         * Returns the position of the current chunk, starts at {@code 1}.
         *
         * @return The position of the current chunk, starts at {@code 1}.
         */
        int position();

        /**
         * Returns the results in the chunk.
         *
         * @return The results in the chunk.
         */
        List<E> getResults();
    }

    /**
     * Creates and returns a new instance of this query. Both queries can be modifies without changing the
     * other.
     *
     * @return The newly created copy.
     */
    RepositoryQuery<K, E> copy();
}
