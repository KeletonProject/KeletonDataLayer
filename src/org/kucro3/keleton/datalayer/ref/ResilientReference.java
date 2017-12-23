package org.kucro3.keleton.datalayer.ref;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Objects;

public class ResilientReference<T> implements Reference<T> {
    public ResilientReference(T object)
    {
        this(object, null);
    }

    public ResilientReference(T object, ReferenceQueue<T> queue)
    {
        this.strongRef = Objects.requireNonNull(object);
        this.queue = queue;
    }

    public boolean isSoft()
    {
        return state;
    }

    public boolean isStrong()
    {
        return !state;
    }

    public boolean setSoft()
    {
        if(isSoft())
            return true;

        softRef = queue == null ? new SoftReference<>(strongRef) : new SoftReference<>(strongRef, queue);
        strongRef = null;

        flip();
        return true;
    }

    public boolean setStrong()
    {
        if(isStrong())
            return true;

        if(softRef.get() == null)
            return false;

        strongRef = softRef.get();
        softRef.clear();
        softRef = null;

        return true;
    }

    void flip()
    {
        state = !state;
    }

    @Override
    public T get()
    {
        if(state)
            return softRef.get();
        return strongRef;
    }

    @Override
    public void clear()
    {
        if(state)
            softRef.clear();
        else
            strongRef = null;
    }

    private ReferenceQueue<T> queue;

    private SoftReference<T> softRef;

    private T strongRef;

    private volatile boolean state;
}
