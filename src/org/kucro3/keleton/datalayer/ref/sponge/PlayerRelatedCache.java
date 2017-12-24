package org.kucro3.keleton.datalayer.ref.sponge;

import org.kucro3.keleton.datalayer.ref.ResilientReferenceGroup;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayerRelatedCache<T> {
    public PlayerRelatedCache(Object plugin)
    {
        this.plugin = plugin;
        this.map = new HashMap<>();
    }

    public Object getPlugin()
    {
        return plugin;
    }

    public BiConsumer<Player, PlayerRelatedCache<T>> getCallbackOnLogin()
    {
        return callbackOnLogin;
    }

    public void setCallbackOnLogin(BiConsumer<Player, PlayerRelatedCache<T>> callbackOnLogin)
    {
        this.callbackOnLogin = callbackOnLogin;
    }

    public BiConsumer<Player, PlayerRelatedCache<T>> getCallbackOnLogoff()
    {
        return callbackOnLogoff;
    }

    public void setCallbackOnLogoff(BiConsumer<Player, PlayerRelatedCache<T>> callbackOnLogoff)
    {
        this.callbackOnLogoff = callbackOnLogoff;
    }

    public synchronized boolean isEnabled()
    {
        return enabled;
    }

    public synchronized boolean enable()
    {
        if(enabled)
            return false;

        Sponge.getEventManager().registerListeners(plugin, this);

        return (enabled = true);
    }

    public synchronized boolean disable()
    {
        if(!enabled)
            return false;

        Sponge.getEventManager().unregisterListeners(this);

        return (enabled = false);
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
        for(Map.Entry<UUID, ResilientReferenceGroup<T>> entry : Collections.unmodifiableMap(map).entrySet())
            biConsumer.accept(entry.getKey(), entry.getValue());
    }

    @Listener
    public void onLogin(ClientConnectionEvent.Join event)
    {
        if(callbackOnLogin != null)
            callbackOnLogin.accept(event.getTargetEntity(), this);
    }

    @Listener
    public void onLogoff(ClientConnectionEvent.Disconnect event)
    {
        if(callbackOnLogoff != null)
            callbackOnLogoff.accept(event.getTargetEntity(), this);
    }

    private boolean enabled;

    private BiConsumer<Player, PlayerRelatedCache<T>> callbackOnLogin;

    private BiConsumer<Player, PlayerRelatedCache<T>> callbackOnLogoff;

    private final Map<UUID, ResilientReferenceGroup<T>> map;

    private final Object plugin;
}
