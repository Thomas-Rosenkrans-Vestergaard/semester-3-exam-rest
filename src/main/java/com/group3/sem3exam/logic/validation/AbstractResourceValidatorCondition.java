package com.group3.sem3exam.logic.validation;

import java.util.function.Function;

public abstract class AbstractResourceValidatorCondition<R, V> implements ResourceValidatorCondition<R, V>
{

    /**
     * The factory that returns the value V from the object R.
     */
    private final Function<R, V> factory;

    /**
     * Creates a new {@link AbstractResourceValidatorCondition},
     *
     * @param factory The factory that returns the value V from the object R.
     */
    public AbstractResourceValidatorCondition(Function<R, V> factory)
    {
        this.factory = factory;
    }

    /**
     * Returns the factory that returns the value V from the object R.
     *
     * @return The factory that returns the value V from the object R.
     */
    @Override
    public Function<R, V> getFactory()
    {
        return factory;
    }
}
