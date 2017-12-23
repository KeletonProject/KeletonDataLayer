package org.kucro3.keleton.datalayer.ref;

import java.lang.ref.ReferenceQueue;

public class WeakReference<T> implements Reference<T> {
    public WeakReference(T object)
    {
        this(object, null);
    }

    public WeakReference(T object, ReferenceQueue<T> queue)
    {
        this.ref = queue == null ? new java.lang.ref.WeakReference<>(object) : new java.lang.ref.WeakReference<>(object, queue);
    }

    public java.lang.ref.WeakReference getRef()
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

    protected final java.lang.ref.WeakReference<T> ref;
}
