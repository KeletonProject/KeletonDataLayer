package org.kucro3.keleton.datalayer.api.home;

import java.util.Objects;
import java.util.UUID;

public class HomeData {
    public HomeData()
    {
    }

    public HomeData(UUID owner, String name, int x, int y, int z, String world)
    {
        this.uuid = owner;
        this.name = name;
        this.location_x = x;
        this.location_y = y;
        this.location_z = z;
        this.location_world = world;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    public UUID getUniqueId()
    {
        return uuid;
    }

    public void setUnqiueId(UUID uuid)
    {
        this.uuid = Objects.requireNonNull(uuid);
    }

    public String getWorld()
    {
        return location_world;
    }

    public void setWorld(String name)
    {
        this.location_world = Objects.requireNonNull(name);
    }

    public int getX()
    {
        return location_x;
    }

    public void setX(int x)
    {
        this.location_x = x;
    }

    public int getY()
    {
        return location_y;
    }

    public void setY(int y)
    {
        this.location_y = y;
    }

    public int getZ()
    {
        return location_z;
    }

    public void setZ(int z)
    {
        this.location_z = z;
    }

    public long getId()
    {
        return id;
    }

    String name;

    UUID uuid;

    String location_world;

    int location_x;

    int location_y;

    int location_z;

    long id;
}
