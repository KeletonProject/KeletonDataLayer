package org.kucro3.keleton.datalayer.ref;

import java.util.Iterator;
import java.util.LinkedList;

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

    public int size()
    {
        return refs.size();
    }

    @Override
    public Iterator<Reference<T>> iterator()
    {
        return refs.iterator();
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
