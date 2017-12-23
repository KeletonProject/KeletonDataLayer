package org.kucro3.keleton.datalayer.ref;

import java.lang.ref.ReferenceQueue;
import java.util.Objects;

public class SoftReference<T> implements Reference<T> {
    public SoftReference(T object)
    {
        this(object, null);
    }

    public SoftReference(T object, ReferenceQueue<T> queue)
    {
        Objects.requireNonNull(object);
        this.ref = queue == null ? new java.lang.ref.SoftReference<>(object) : new java.lang.ref.SoftReference<>(object, queue);
    }

    public java.lang.ref.SoftReference<T> getRef()
    {
        return ref;
    }

    @Override
    public T get()
    {
        return ref.get();
    }

    @Override
    public void clear()
    {
        ref.clear();
    }

    protected final java.lang.ref.SoftReference<T> ref;
}
