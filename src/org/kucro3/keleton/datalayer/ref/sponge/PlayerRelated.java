package org.kucro3.keleton.datalayer.ref.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.function.BiConsumer;

public abstract class PlayerRelated<P extends PlayerRelated> {
    protected PlayerRelated(Object plugin)
    {
        this.plugin = plugin;
    }

    public Object getPlugin()
    {
        return plugin;
    }

    @Listener
    public void onLogin(ClientConnectionEvent.Join event)
    {
        if(callbackOnLogin != null)
            callbackOnLogin.accept(event.getTargetEntity(), (P) this);
    }

    @Listener
    public void onLogoff(ClientConnectionEvent.Disconnect event)
    {
        if(callbackOnLogoff != null)
            callbackOnLogoff.accept(event.getTargetEntity(), (P) this);
    }

    public BiConsumer<Player, P> getCallbackOnLogin()
    {
        return callbackOnLogin;
    }

    public void setCallbackOnLogin(BiConsumer<Player, P> callbackOnLogin)
    {
        this.callbackOnLogin = callbackOnLogin;
    }

    public BiConsumer<Player, P> getCallbackOnLogoff()
    {
        return callbackOnLogoff;
    }

    public void setCallbackOnLogoff(BiConsumer<Player, P> callbackOnLogoff)
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

    private BiConsumer<Player, P> callbackOnLogin;

    private BiConsumer<Player, P> callbackOnLogoff;

    protected final Object plugin;

    private boolean enabled;
}
