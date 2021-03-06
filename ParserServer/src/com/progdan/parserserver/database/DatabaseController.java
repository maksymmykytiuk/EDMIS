package com.progdan.parserserver.database;

import java.sql.*;

import com.progdan.logengine.*;

public abstract class DatabaseController {
    private static Logger logger = Logger.getLogger(DatabaseController.class.
            getName());
    public abstract void testDriver();

    public abstract Connection createDatabase(String dbserver, String dbname);

    public abstract Connection getConnection();

    public abstract Connection getConnection(String dbserver, String dbname,
                                             String user, String passwd);

    public int executeUpdate(Connection conn, String sql) {
        logger.debug(">>> Start of DatabaseController.executeUpdate()***");
        int n = -1;
        try {
            Statement s = conn.createStatement();
            n = s.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.debug("<<< End of DatabaseController.executeUpdate()***");
        return n;
    }

    public ResultSet executeQuery(Connection conn, String sql) {
        logger.debug(">>> Start of DatabaseController.executeQuery()***");
        ResultSet rs = null;
        try {
            Statement s = conn.createStatement();
            rs = s.executeQuery(sql);
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.debug("<<< End of DatabaseController.executeQuery()***");
        return rs;
    }
}
