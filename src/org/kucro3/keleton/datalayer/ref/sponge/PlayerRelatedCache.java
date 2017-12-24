package org.kucro3.keleton.datalayer.ref.sponge;

import org.kucro3.keleton.datalayer.ref.ResilientReference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayerRelatedCache<T> extends PlayerRelated<PlayerRelatedCache<T>> {
    public PlayerRelatedCache(Object plugin)
    {
        super(plugin);
    }

    static <T> T unpack(ResilientReference<T> ref)
    {
        if(ref == null)
            return null;
        return ref.get();
    }

    public void foreach(BiConsumer<UUID, ResilientReference<T>> biConsumer)
    {
        Iterator<Map.Entry<UUID, ResilientReference<T>>> iter = refs.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry<UUID, ResilientReference<T>> entry = iter.next();
            if(entry.getValue().available())
                biConsumer.accept(entry.getKey(), entry.getValue());
            else
                iter.remove();
        }
    }

    public boolean computeIfPresent(UUID uuid, Consumer<ResilientReference<T>> consumer)
    {
        ResilientReference<T> ref = refs.get(uuid);
        if(ref != null && ref.available())
            consumer.accept(ref);
        else
            return false;
        return true;
    }

    public void clear()
    {
        refs.clear();
    }

    public boolean contains(UUID uuid)
    {
        return get(uuid) != null;
    }

    public T get(UUID uuid)
    {
        return unpack(refs.get(uuid));
    }

    public ResilientReference<T> getReference(UUID uuid)
    {
        return refs.get(uuid);
    }

    public T set(UUID uuid, T object)
    {
       return unpack(refs.put(uuid, new ResilientReference<>(object)));
    }

    public T remove(UUID uuid)
    {
        return unpack(refs.remove(uuid));
    }

    public boolean setSoft(UUID uuid)
    {
        return computeIfPresent(uuid, (ref) -> ref.setSoft());
    }

    public boolean setStrong(UUID uuid)
    {
        return computeIfPresent(uuid, (ref) -> ref.setStrong());
    }

    private final HashMap<UUID, ResilientReference<T>> refs = new HashMap<>();
}
