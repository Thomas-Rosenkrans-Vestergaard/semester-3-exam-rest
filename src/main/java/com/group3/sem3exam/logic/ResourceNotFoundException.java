package com.group3.sem3exam.logic;

public class ResourceNotFoundException extends FacadeException
{

    /**
     * The type of the missing resource.
     */
    private final Class resource;

    /**
     * The id of the missing resource.
     */
    private final Object id;

    /**
     * Creates a new {@link ResourceNotFoundException}.
     *
     * @param resource The class representing the resource that could not be found.
     * @param id       The id of the missing resource.
     */
    public ResourceNotFoundException(Class resource, Object id)
    {
        this(resource, id, 404);
    }

    /**
     * Creates a new {@link ResourceNotFoundException} with a http status code.
     *
     * @param resource The class representing the resource that could not be found.
     * @param id       The id of the missing resource.
     * @param code     The status code to signal to the end user.
     */
    public ResourceNotFoundException(Class resource, Object id, int code)
    {
        this(resource, id, code, null);
    }

    /**
     * Creates a new {@link ResourceNotFoundException} with a status code and a throwable.
     *
     * @param resource The class representing the resource that could not be found.
     * @param id       The id of the missing resource.
     * @param code     The status code to signal to the end user.
     * @param cause    The cause of this exception.
     */
    public ResourceNotFoundException(Class resource, Object id, int code, Throwable cause)
    {
        super(
                String.format("%sNotFoundError", resource.getSimpleName()),
                String.format("The %s resource with the provided key %s could not be found.", id.toString()),
                code,
                cause);

        this.resource = resource;
        this.id = id;
    }

    /**
     * Creates and returns a new {@link ResourceNotFoundException}. The status code of the returned exception is
     * {@code 404}.
     *
     * @param resource The class representing the resource that could not be found.
     * @param id       The id of the missing resource.
     * @return The created exception.
     */
    public static ResourceNotFoundException with404(Class resource, Object id)
    {
        return new ResourceNotFoundException(resource, id, 404);
    }

    /**
     * Creates and returns a new {@link ResourceNotFoundException}. The status code of the returned exception is
     * {@code 400}.
     *
     * @param resource The class representing the resource that could not be found.
     * @param id       The id of the missing resource.
     * @return The created exception.
     */
    public static ResourceNotFoundException with400(Class resource, Object id)
    {
        return new ResourceNotFoundException(resource, id, 400);
    }

    /**
     * Returns the type of the missing resource.
     *
     * @return The type of the missing resource.
     */
    public Class getResource()
    {
        return this.resource;
    }

    /**
     * Returns the id of the missing resource.
     *
     * @return The id of the missing resource.
     */
    public Object getId()
    {
        return this.id;
    }
}
