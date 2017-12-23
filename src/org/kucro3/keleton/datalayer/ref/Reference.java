package org.kucro3.keleton.datalayer.ref;

public interface Reference<T> {
    public T get();

    public default boolean available()
    {
        return get() != null;
    }

    public void clear();
}
