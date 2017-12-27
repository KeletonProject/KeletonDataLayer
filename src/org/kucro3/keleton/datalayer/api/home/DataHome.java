package org.kucro3.keleton.datalayer.api.home;

import org.kucro3.keleton.datalayer.Misc;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class DataHome {
    public DataHome()
    {
    }

    public static Optional<DataHome> query(Connection db, String tableName, UUID owner, String name)
    {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT * FROM " + tableName + " WHERE UID=? AND NAME=?");

            ps.setObject(1, owner);
            ps.setNString(2, name);

            return fromResult(ps.executeQuery());
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<DataHome> query(Connection db, String tableName, UUID owner, String name, String world)
    {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT * FROM " + tableName + " WHERE UID=? AND NAME=? AND LOCATION_W=?");

            ps.setObject(1, owner);
            ps.setNString(2, name);
            ps.setNString(3, world);

            return fromResult(ps.executeQuery());
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    static Optional<DataHome> fromResult(ResultSet result) throws SQLException
    {
        if(result.next())
        {
            DataHome entity = new DataHome();
            consume(result, entity);
            return Optional.of(entity);
        }
        else
            return Optional.empty();
    }

    public static boolean deleteAll(Connection db, String tableName) throws SQLException
    {
        return Misc.operate(db, "DELETE FROM " + tableName, (p) -> p.executeUpdate(), true);
    }

    public static boolean delete(Connection db, String tableName, UUID uid) throws SQLException
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE UID=?", (p) -> {
            p.setObject(1, uid);

            p.executeUpdate();
        }, true);
    }

    public static boolean delete(Connection db, String tableName, UUID uid, String name) throws SQLException
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE NAME=? AND UID=?", (p) -> {
            p.setNString(1, name);
            p.setObject(2, uid);

            p.executeUpdate();
        }, true);
    }

    public static boolean delete(Connection db, String tableName, String world) throws SQLException
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE LOCATION_W=?", (p) -> {
            p.setNString(1, world);

            p.executeUpdate();
        }, true);
    }

    public static boolean fastDelete(Connection db, String tableName, DataHome dataEntity) throws SQLException
    {
        return fastDelete0(db, tableName, dataEntity.getId());
    }

    static boolean fastDelete0(Connection db, String tableName, long id) throws SQLException
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE ID=?", (p) -> {
            p.setLong(1, id);

            p.executeUpdate();
        }, true);
    }

    public static boolean loadAll(Connection db, String tableName, Consumer<DataHome> consumer) throws SQLException
    {
        return load(db, "SELECT * FROM " + tableName, consumer);
    }

    public static boolean load(Connection db, String tableName, UUID uid, Consumer<DataHome> consumer) throws SQLException
    {
        return load(db, "SELECT * FROM " + tableName + " WHERE UID=?", consumer, uid);
    }

    static boolean load(Connection db, String sql, Consumer<DataHome> consumer, Object... args) throws SQLException
    {
        ResultSet result;
        try {
            PreparedStatement ps = db.prepareStatement(sql);
            for(int i = 0; i < args.length; )
                ps.setObject(++i, args[i - 1]);
            result = ps.executeQuery();
        } catch (SQLException e) {
            return false;
        }
        if(result == null)
            return false;
        return load0(result, consumer);
    }

    static boolean load0(ResultSet results, Consumer<DataHome> consumer) throws SQLException
    {
        try {
            while (results.next())
            {
                DataHome entity = new DataHome();

                consume(results, entity);
                consumer.accept(entity);
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    static void consume(ResultSet results, DataHome entity) throws SQLException
    {
        entity.id = results.getLong("ID");
        entity.uuid = results.getObject("UID", UUID.class);
        entity.name = results.getNString("NAME");
        entity.location_world = results.getNString("LOCATION_W");
        entity.location_x = results.getInt("LOCATION_X");
        entity.location_y = results.getInt("LOCATION_Y");
        entity.location_z = results.getInt("LOCATION_Z");
    }

    public static boolean ensureTable(Connection db, String tableName) throws SQLException
    {
        return Misc.operate(db,
                "CREATE TABLE IF NOT EXISTS (" + tableName +
                "ID BIGINT NOT NULL AUTO_INCREMENT," +
                "UID UUID NOT NULL," +
                "NAME NVARCHAR(256) NOT NULL," +
                "LOCATION_W NVARCAHR(256) NOT NULL," +
                "LOCATION_X INTEGER," +
                "LOCATION_Y INTEGER," +
                "LOCATION_Z INTEGER," +
                "PRIMARY KEY (ID)" +
                ")", (p) -> {}, true);
    }

    public synchronized boolean remove(Connection db, String tableName) throws SQLException
    {
        return Misc.operate(db,
                "DELETE FROM " + tableName + " WHERE NAME=? AND UID=?", (p) -> {
            p.setNString(1, name);
            p.setObject(2, uuid);

            p.executeUpdate();
        }, true);
    }

    public synchronized boolean insert(Connection db, String tableName) throws SQLException
    {
        return Misc.operate(db,
                "INSERT INTO " + tableName + " SET (UID, NAME, LOCATION_W, LOCATION_X, LOCATION_Y, LOCATION_Z) " +
                    "VALUES (?, ?, ?, ?, ?, ?)", (p) -> {
            p.setObject(1, uuid);
            p.setNString(2, name);
            p.setNString(3, location_world);
            p.setInt(4, location_x);
            p.setInt(5, location_y);
            p.setInt(6, location_z);

            p.executeUpdate();
        }, true);
    }

    public synchronized boolean update(Connection db, String tableName) throws SQLException
    {
        return Misc.operate(db,
                "UPDATE " + tableName + " SET LOCATION_W=?, LOCATION_X=?, LOCATION_Y=?, LOCATION_Z=? WHERE NAME=? AND UID=?", (p) -> {
            p.setNString(1, location_world);
            p.setInt(2, location_x);
            p.setInt(3, location_y);
            p.setInt(4, location_z);

            p.setNString(5, name);
            p.setObject(6, uuid);

            p.executeUpdate();
        }, true);
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

    private String name;

    private UUID uuid;

    private String location_world;

    private int location_x;

    private int location_y;

    private int location_z;

    private long id;
}
