package org.kucro3.keleton.datalayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Misc {
    public static boolean operate(Connection db, String sql, StatementOperation operation, boolean sfm) throws SQLException
    {
        PreparedStatement p = null;
        try {
            if(sfm)
                db.setAutoCommit(false);

            p = db.prepareStatement(sql);
            operation.accept(p);

            if(sfm)
                db.commit();

            return true;
        } catch (SQLException e) {
            try {
                if(sfm)
                    db.rollback();
            } catch (SQLException e1) {
                // unused
                throw new InternalError(e);
            }

            throw e;
        } finally {
            try {
                db.setAutoCommit(true);
            } catch (SQLException e) {
                // unused
                throw new InternalError(e);
            }

            if(p != null)
                try {
                    p.close();
                } catch (SQLException e) {
                    return false;
                }
        }
    }

    public interface StatementOperation
    {
        void accept(PreparedStatement p) throws SQLException;
    }
}
