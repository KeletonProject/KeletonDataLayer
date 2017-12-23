package org.kucro3.keleton.datalayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Misc {
    public static boolean operate(Connection db, String sql, StatementOperation operation)
    {
        PreparedStatement p = null;
        try {
            p = db.prepareStatement(sql);
            operation.accept(p);
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
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
