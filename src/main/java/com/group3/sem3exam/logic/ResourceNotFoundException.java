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
     * @param cause    The cause of this error.
     */
    public ResourceNotFoundException(Class resource, Object id, int code, Throwable cause)
    {
        super(
                String.format("%sNotFoundError", resource.getSimpleName()),
                String.format("The %s resource with the provided key %s could not be found.",
                              resource.getSimpleName(),
                              id.toString()),
                cause);

        this.resource = resource;
        this.id = id;
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
