package org.kucro3.keleton.datalayer.ref;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ReferenceIterator<T, R extends Reference<T>> implements Iterator<R> {
    ReferenceIterator(ListIterator<R> iter)
    {
        this.iter = iter;
    }

    @Override
    public boolean hasNext()
    {
        if(iter.hasNext())
        {
            while(iter.hasNext() && !iter.next().available())
                iter.remove();
            iter.previous();
            return iter.hasNext();
        }
        else
            return false;
    }

    @Override
    public R next()
    {
        if(!hasNext())
            throw new NoSuchElementException();
        return iter.next();
    }

    @Override
    public void remove()
    {
        iter.remove();
    }

    private final ListIterator<R> iter;
}
