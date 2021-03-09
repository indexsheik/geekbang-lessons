package org.geektimes.projects.user.sql;

import org.geektimes.projects.user.context.ComponentContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionManager {

    private static Logger logger = Logger.getLogger(DBConnectionManager.class.getName());

    private Connection connection;

//    private static DBConnectionManager instance = new DBConnectionManager();
//
//    public static DBConnectionManager getInstance() {
//        return instance;
//    }

    public DBConnectionManager() {
        initConnection();
    }

    private void initConnection() {
        try {
            DataSource dataSource = ComponentContext.getInstance().getComponent("jdbc/UserPlatformDB");
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("db connect failed.%s", e.getMessage()));
            throw new RuntimeException();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void releaseConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }
}
