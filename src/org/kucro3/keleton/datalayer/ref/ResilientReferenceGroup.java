package org.kucro3.keleton.datalayer.ref;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
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

    @Override
    public Iterator<ResilientReference<T>> iterator()
    {
        return refs.iterator();
    }

    public int setStrong()
    {
        return foreach((r) -> r.setStrong());
    }

    public int setSoft()
    {
        return foreach((r) -> r.setSoft());
    }

    int foreach(Consumer<ResilientReference<T>> consumer)
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
                consumer.accept(ref);
                i++;
            }

        return i;
    }

    protected final LinkedList<ResilientReference<T>> refs;
}
