package org.kucro3.keleton.datalayer.ref;

import org.kucro3.keleton.function.Remove;

import java.util.function.BiConsumer;

public interface ReferenceGroup<T, R extends Reference<T>> extends Iterable<R> {
    public void clear();

    public void clearReferences();

    public void add(R ref);

    public int size();

    public boolean remove(R ref);

    public int foreach(BiConsumer<R, Remove> consumer);
}
