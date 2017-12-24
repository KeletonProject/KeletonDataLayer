package org.kucro3.keleton.datalayer.ref;

import java.lang.ref.ReferenceQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    int foreach(BiConsumer<ResilientReference<T>, ListIterator<ResilientReference<T>>> consumer)
    {
        int i = 0;
        ListIterator<ResilientReference<T>> iter = refs.listIterator();

        if(!iter.hasNext())
            return 0;

        ResilientReference<T> ref = iter.next();
        for(; iter.hasNext(); ref = iter.next())
            if (!ref.available())
                iter.remove();
            else {
                consumer.accept(ref, iter);
                i++;
            }

        return i;
    }

    protected final LinkedList<ResilientReference<T>> refs;
}
