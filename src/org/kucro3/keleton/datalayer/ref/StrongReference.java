package org.kucro3.keleton.datalayer.ref;

import java.util.Objects;

public class StrongReference<T> implements Reference<T> {
    public StrongReference(T object)
    {
        this.ref = Objects.requireNonNull(object);
    }

    @Override
    public T get()
    {
        return ref;
    }

    @Override
    public void clear()
    {
        ref = null;
    }

    protected T ref;
}
