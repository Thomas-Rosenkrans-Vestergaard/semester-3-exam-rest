package com.group3.sem3exam.data.repositories.queries;

import com.group3.sem3exam.data.repositories.queries.tree.Direction;

import java.util.List;

/**
 * Represents a query on a repository.
 * <p>
 * Constraints are added to the query using the {where} prefixed methods.
 * The query is executed using the {get} prefixed methods.
 *
 * Any operation not supported by an implementation throws an {@link UnsupportedOperationException}.
 *
 * @param <E> The type of the entity handled by this query.
 */
public interface RepositoryQuery<E>
{

    /**
     * Adds an {@code equals} constraint to the query. The value of the provided {@code column} must be equal to the
     * provided {@code object}.
     *
     * @param column The column value that must be equal to the object.
     * @param object The object the column must equal.
     * @return this
     * @see RepositoryQuery#whereNot(String column, Object object) The inverse of this operation.
     */
    RepositoryQuery whereEq(String column, Object object);

    /**
     * Adds a {@code not equals} constraint to the query. The value of the provided {@code column} must not be equal to the
     * provided {@code object}.
     *
     * @param column The column value that must not be equal to the object.
     * @param object The object the column must not equal.
     * @return this
     * @see RepositoryQuery#whereEq(String, Object) The inverse of this operation.
     */
    RepositoryQuery whereNot(String column, Object object);

    /**
     * Adds an {@code in} constraint to the query. The value of the provided {@code column} must be present in the
     * provided list of {@code objects}.
     *
     * @param column  The column value that must be present in the provided {@code objects}.
     * @param objects The objects that the value of the column must match.
     * @return this
     * @see RepositoryQuery#whereNotIn(String, Object...) The inverse of this operation.
     */
    RepositoryQuery whereIn(String column, Object... objects);

    /**
     * Adds an {@code in} constraint to the query. The value of the provided {@code column} must be present in the
     * provided list of {@code objects}.
     *
     * @param column  The column value that must be present in the provided {@code objects}.
     * @param objects The objects that the value of the column must match.
     * @return this
     * @see RepositoryQuery#whereNotIn(String, List) The inverse of this operation.
     */
    RepositoryQuery whereIn(String column, List<Object> objects);

    /**
     * Adds a {@code not in} constraint to the query. The value of the provided {@code column} must not be present in
     * the provided list of {@code objects}.
     *
     * @param column  The column value that must not be present in the provided {@code objects}.
     * @param objects The objects that the value of the column must not match.
     * @return this
     * @see RepositoryQuery#whereIn(String, Object...) The inverse of this operation.
     */
    RepositoryQuery whereNotIn(String column, Object... objects);

    /**
     * Adds a {@code not in} constraint to the query. The value of the provided {@code column} must not be present in
     * the provided list of {@code objects}.
     *
     * @param column  The column value that must not be present in the provided {@code objects}.
     * @param objects The objects that the value of the column must not match.
     * @return this
     * @see RepositoryQuery#whereIn(String, Object...) The inverse of this operation.
     */
    RepositoryQuery whereNotIn(String column, List<Object> objects);

    /**
     * Adds a {@code between} constraint to the query. The value of the provided {@code column} must be between
     * {@code start} and {@code end}. Whether or not the operation is inclusive is implementation specific.
     *
     * @param column The value of the column that must be between the provided {@code start} and {@code end}.
     * @param start  The start of the between constraint.
     * @param end    The end of the between constraint.
     * @return this
     * @see RepositoryQuery#whereOutside(String, Object, Object) The inverse operation.
     */
    RepositoryQuery whereBetween(String column, Object start, Object end);

    /**
     * Adds a {@code not between} constraint to the query. The value of the provided {@code column} must not be between
     * {@code start} and {@code end}. Whether or not the operation is inclusive is implementation specific.
     *
     * @param column The value of the column that must not be between the provided {@code start} and {@code end}.
     * @param start  The start of the between constraint.
     * @param end    The end of the between constraint.
     * @return this
     * @see RepositoryQuery#whereBetween(String, Object, Object) The inverse of this operation.
     */
    RepositoryQuery whereOutside(String column, Object start, Object end);

    /**
     * Adds a {@code like} constraint to the query. The value of the provided {@code column} must be similar to the
     * provided {@code object}. The behavior of this constraint is implementation specific.
     *
     * @param column The value of the column that must be {@code like} the provided {@code object}.
     * @param object The value that the column value must be {@code like}
     * @return this
     * @see RepositoryQuery#whereNotLike(String, Object) The inverse of this operation.
     */
    RepositoryQuery whereLike(String column, Object object);

    /**
     * Adds a {@code like} constraint to the query. The value of the provided {@code column} must not be similar to
     * the provided {@code object}. The behavior of this constraint is implementation specific.
     *
     * @param column The value of the column that must not be {@code like} the provided {@code object}.
     * @param object The value that the column value not must be {@code like}.
     * @return this
     * @see RepositoryQuery#whereLike(String, Object) The inverse of this operation.
     */
    RepositoryQuery whereNotLike(String column, Object object);

    /**
     * Orders the provided {@code column} in the provided {@code direction}.
     *
     * @param column    The column to order by.
     * @param direction The direction the column should be ordered in.
     * @return this
     * @see RepositoryQuery#desc(String) A cleaner way to order by DESC.
     * @see RepositoryQuery#asc(String) A cleaner way to order by ASC.
     */
    RepositoryQuery order(String column, Direction direction);

    /**
     * Orders the provided {@code column} to descending.
     *
     * @param column The column to order by.
     * @return this
     */
    default RepositoryQuery desc(String column)
    {
        return order(column, Direction.DESC);
    }

    /**
     * Orders the provided {@code column} to ascending.
     *
     * @param column The column to order by.
     * @return this
     */
    default RepositoryQuery asc(String column)
    {
        return order(column, Direction.ASC);
    }

    /**
     * Executes the query, returning all results.
     *
     * @return All the results in the query.
     */
    List<E> getAll();

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
    E get(int n);

    /**
     * Returns the first result of the query.
     *
     * @return The first result of the query. {@code null} when the result set is empty.
     */
    default E getFirst()
    {
        return get(1);
    }
}
