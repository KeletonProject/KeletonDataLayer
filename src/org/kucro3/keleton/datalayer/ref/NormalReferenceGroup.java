package org.kucro3.keleton.datalayer.ref;

import org.kucro3.keleton.function.Remove;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;

public class NormalReferenceGroup<T> implements ReferenceGroup<T, Reference<T>> {
    public NormalReferenceGroup()
    {
        this.refs = new LinkedList<>();
    }

    public void add(Reference<T> ref)
    {
        refs.addLast(ref);
    }

    public boolean remove(Reference<T> ref)
    {
        return refs.remove(ref);
    }

    @Override
    public int foreach(BiConsumer<Reference<T>, Remove> consumer)
    {
        return foreach(refs, consumer);
    }

    protected static <T, R extends Reference<T>> int foreach(List<R> refs, BiConsumer<R, Remove> consumer)
    {
        int i = 0;
        ListIterator<R> iter = refs.listIterator();

        if(!iter.hasNext())
            return 0;

        R ref = iter.next();
        for(; iter.hasNext(); ref = iter.next())
            if (!ref.available())
                iter.remove();
            else {
                consumer.accept(ref, () -> iter.remove());
                i++;
            }

        return i;
    }

    @Override
    public int size()
    {
        return resize();
    }

    int resize()
    {
        foreach((refs, remove) -> {});
        return refs.size();
    }

    @Override
    public Iterator<Reference<T>> iterator()
    {
        return new ReferenceIterator<>(refs.listIterator());
    }

    public void clear()
    {
        refs.clear();
    }

    public void clearReferences()
    {
        for(Reference<T> ref : this)
            ref.clear();
    }

    protected final LinkedList<Reference<T>> refs;
}
