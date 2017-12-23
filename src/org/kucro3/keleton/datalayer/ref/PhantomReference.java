package org.kucro3.keleton.datalayer.ref;

import java.lang.ref.ReferenceQueue;

public class PhantomReference<T> implements Reference<T> {
    public PhantomReference(T object, ReferenceQueue<T> queue)
    {
        this.ref = new java.lang.ref.PhantomReference<>(object, queue);
    }

    public java.lang.ref.PhantomReference getRef()
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

    protected final java.lang.ref.PhantomReference<T> ref;
}
