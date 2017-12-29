package org.kucro3.keleton.datalayer.api.home;

import org.kucro3.keleton.datalayer.Misc;
import org.kucro3.keleton.world.home.exception.HomeStorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public final class HomeStorage {
    private HomeStorage()
    {
    }

    public static Optional<HomeData> query(Connection db, String tableName, UUID owner, String name)
    {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT * FROM " + tableName + " WHERE UID=? AND NAME=?");

            ps.setObject(1, owner);
            ps.setNString(2, name);

            return fromResult(ps.executeQuery());
        } catch (SQLException e) {
            throw new HomeStorageException(e);
        }
    }

    public static Optional<HomeData> query(Connection db, String tableName, UUID owner, String name, String world)
    {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT * FROM " + tableName + " WHERE UID=? AND NAME=? AND LOCATION_W=?");

            ps.setObject(1, owner);
            ps.setNString(2, name);
            ps.setNString(3, world);

            return fromResult(ps.executeQuery());
        } catch (SQLException e) {
            throw new HomeStorageException(e);
        }
    }

    static Optional<HomeData> fromResult(ResultSet result)
    {
        try {
            if (result.next()) {
                HomeData entity = new HomeData();
                consume(result, entity);
                return Optional.of(entity);
            } else
                return Optional.empty();
        } catch (SQLException e) {
            throw new HomeStorageException(e);
        }
    }

    public static boolean deleteAll(Connection db, String tableName)
    {
        return Misc.operate(db, "DELETE FROM " + tableName, (p) -> p.executeUpdate(), true);
    }

    public static boolean delete(Connection db, String tableName, UUID uid)
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE UID=?", (p) -> {
            p.setObject(1, uid);

            p.executeUpdate();
        }, true);
    }

    public static boolean delete(Connection db, String tableName, UUID uid, String name)
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE NAME=? AND UID=?", (p) -> {
            p.setNString(1, name);
            p.setObject(2, uid);

            p.executeUpdate();
        }, true);
    }

    public static boolean delete(Connection db, String tableName, String world)
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE LOCATION_W=?", (p) -> {
            p.setNString(1, world);

            p.executeUpdate();
        }, true);
    }

    public static boolean fastDelete(Connection db, String tableName, HomeData dataEntity)
    {
        return fastDelete0(db, tableName, dataEntity.getId());
    }

    static boolean fastDelete0(Connection db, String tableName, long id)
    {
        return Misc.operate(db, "DELETE FROM " + tableName + " WHERE ID=?", (p) -> {
            p.setLong(1, id);

            p.executeUpdate();
        }, true);
    }

    public static boolean loadAll(Connection db, String tableName, Consumer<HomeData> consumer)
    {
        return load(db, "SELECT * FROM " + tableName, consumer);
    }

    public static boolean load(Connection db, String tableName, UUID uid, Consumer<HomeData> consumer)
    {
        return load(db, "SELECT * FROM " + tableName + " WHERE UID=?", consumer, uid);
    }

    static boolean load(Connection db, String sql, Consumer<HomeData> consumer, Object... args)
    {
        try {
            ResultSet result;

            PreparedStatement ps = db.prepareStatement(sql);
            for (int i = 0; i < args.length; )
                ps.setObject(++i, args[i - 1]);
            result = ps.executeQuery();

            if (result == null)
                return false;
            return load0(result, consumer);
        } catch (SQLException e) {
            throw new HomeStorageException(e);
        }
    }

    static boolean load0(ResultSet results, Consumer<HomeData> consumer)
    {
        try {
            while (results.next()) {
                HomeData entity = new HomeData();

                consume(results, entity);
                consumer.accept(entity);
            }
            return true;
        } catch (SQLException e) {
            throw new HomeStorageException(e);
        }
    }

    static void consume(ResultSet results, HomeData entity)
    {
        try {
            synchronized (entity) {
                entity.id = results.getLong("ID");
                entity.uuid = results.getObject("UID", UUID.class);
                entity.name = results.getNString("NAME");
                entity.location_world = results.getNString("LOCATION_W");
                entity.location_x = results.getInt("LOCATION_X");
                entity.location_y = results.getInt("LOCATION_Y");
                entity.location_z = results.getInt("LOCATION_Z");
            }
        } catch (SQLException e) {
            throw new HomeStorageException(e);
        }
    }

    public synchronized boolean remove(Connection db, String tableName, HomeData entity)
    {
        return fastDelete(db, tableName, entity);
    }

    public static boolean insert(Connection db, String tableName, HomeData entity)
    {
        synchronized (entity) {
            return Misc.operate(db,
                    "INSERT INTO " + tableName + " SET (UID, NAME, LOCATION_W, LOCATION_X, LOCATION_Y, LOCATION_Z) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", (p) -> {
                        p.setObject(1, entity.uuid);
                        p.setNString(2, entity.name);
                        p.setNString(3, entity.location_world);
                        p.setInt(4, entity.location_x);
                        p.setInt(5, entity.location_y);
                        p.setInt(6, entity.location_z);

                        p.executeUpdate();
                    }, true);
        }
    }

    public static boolean update(Connection db, String tableName, HomeData entity)
    {
        synchronized(entity) {
            return Misc.operate(db,
                    "UPDATE " + tableName + " SET LOCATION_W=?, LOCATION_X=?, LOCATION_Y=?, LOCATION_Z=? WHERE NAME=? AND UID=?", (p) -> {
                        p.setNString(1, entity.location_world);
                        p.setInt(2, entity.location_x);
                        p.setInt(3, entity.location_y);
                        p.setInt(4, entity.location_z);

                        p.setNString(5, entity.name);
                        p.setObject(6, entity.uuid);

                        p.executeUpdate();
                    }, true);
        }
    }

    public static boolean ensureTable(Connection db, String tableName)
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
}
