package org.kucro3.keleton.datalayer.ref.sponge;

import org.kucro3.keleton.datalayer.ref.ResilientReferenceGroup;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayerRelatedGroupCache<T> extends PlayerRelated<PlayerRelatedGroupCache<T>> {
    public PlayerRelatedGroupCache(Object plugin)
    {
        super(plugin);
        this.map = new HashMap<>();
    }

    public ResilientReferenceGroup<T> addGroup(UUID uuid)
    {
        ResilientReferenceGroup<T> refs = new ResilientReferenceGroup<>();
        addGroup(uuid, refs);
        return refs;
    }

    public void addGroup(UUID uuid, ResilientReferenceGroup<T> refs)
    {
        map.put(uuid, refs);
    }

    public ResilientReferenceGroup<T> removeGroup(UUID uuid)
    {
        return map.remove(uuid);
    }

    public ResilientReferenceGroup<T> getGroup(UUID uuid)
    {
        return map.get(uuid);
    }

    public boolean hasGroup(UUID uuid)
    {
        return map.containsKey(uuid);
    }

    boolean computeIfPresent(UUID uuid, Consumer<ResilientReferenceGroup<T>> consumer)
    {
        Optional<ResilientReferenceGroup<T>> opt;
        (opt = Optional.ofNullable(map.get(uuid))).ifPresent(consumer);
        return opt.isPresent();
    }

    public void clear()
    {
        map.clear();
    }

    public void clearGroups()
    {
        foreach((k, v) -> v.clear());
    }

    public boolean clearGroup(UUID uuid)
    {
        return computeIfPresent(uuid, (refs) -> refs.clear());
    }

    public void clearReferences()
    {
        foreach((k, v) -> v.clearReferences());
    }

    public boolean clearReferencesOf(UUID uuid)
    {
        return computeIfPresent(uuid, (refs) -> refs.clearReferences());
    }

    public void setSoft()
    {
        foreach((k, v) -> v.setSoft());
    }

    public boolean setSoft(UUID uuid)
    {
        return computeIfPresent(uuid, (refs) -> refs.setSoft());
    }

    public void setStrong()
    {
        foreach((k, v) -> v.setStrong());
    }

    public boolean setStrong(UUID uuid)
    {
        return computeIfPresent(uuid, (refs) -> refs.setStrong());
    }

    public void foreach(BiConsumer<UUID, ResilientReferenceGroup<T>> biConsumer)
    {
        for (Map.Entry<UUID, ResilientReferenceGroup<T>> entry : Collections.unmodifiableMap(map).entrySet())
            biConsumer.accept(entry.getKey(), entry.getValue());
    }

    private final Map<UUID, ResilientReferenceGroup<T>> map;
}
