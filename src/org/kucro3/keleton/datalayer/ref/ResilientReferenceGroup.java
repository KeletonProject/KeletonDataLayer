package org.kucro3.keleton.datalayer.ref;

import org.kucro3.keleton.function.Remove;

import java.lang.ref.ReferenceQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiConsumer;

public class ResilientReferenceGroup<T> implements ReferenceGroup<T, ResilientReference<T>> {
    public ResilientReferenceGroup()
    {
        this.refs = new LinkedList<>();
    }

    @Override
    public void clear()
    {
        refs.clear();
    }

    @Override
    public void clearReferences()
    {
        for(ResilientReference<T> ref : this)
            ref.clear();
    }

    @Override
    public void add(ResilientReference<T> ref)
    {
        refs.addLast(ref);
    }

    public void add(T object)
    {
        add(new ResilientReference<>(object));
    }

    public void add(T object, ReferenceQueue<T> queue)
    {
        add(new ResilientReference<>(object, queue));
    }

    @Override
    public int size()
    {
        return resize();
    }

    int resize()
    {
        foreach((refs, iter) -> {});
        return refs.size();
    }

    @Override
    public boolean remove(ResilientReference<T> ref)
    {
        return refs.remove(ref);
    }

    public boolean remove(T object)
    {
        final StrongReference<Boolean> ref = new StrongReference<>(false);

        foreach((r, iter) -> {
           if(ref.get() == object)
           {
               iter.remove();
               ref.set(true);
           }
        });

        return ref.get();
    }

    @Override
    public Iterator<ResilientReference<T>> iterator()
    {
        return refs.iterator();
    }

    public int setStrong()
    {
        return foreach((r, iter) -> r.setStrong());
    }

    public int setSoft()
    {
        return foreach((r, iter) -> r.setSoft());
    }

    @Override
    public int foreach(BiConsumer<ResilientReference<T>, Remove> consumer)
    {
        return NormalReferenceGroup.foreach(refs, consumer);
    }

    protected final LinkedList<ResilientReference<T>> refs;
}
