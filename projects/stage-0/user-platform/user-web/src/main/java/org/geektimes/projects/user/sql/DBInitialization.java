package org.geektimes.projects.user.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @Author: Xiao Xuezhi
 * @Date: 2021/3/3 23:06
 * @Version: 1.0
 */
public class DBInitialization {

    private static Logger logger = Logger.getLogger(DBInitialization.class.getName());

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
            "('A','******','a@gmail.com','1') , " +
            "('B','******','b@gmail.com','2') , " +
            "('C','******','c@gmail.com','3') , " +
            "('D','******','d@gmail.com','4') , " +
            "('E','******','e@gmail.com','5')";

    public static void init() throws SQLException {
        String databaseURL = "jdbc:derby:/db/user-platform;create=true";
        Connection connection = DriverManager.getConnection(databaseURL);

        Statement statement = connection.createStatement();
        try {
            // 删除 users 表
            statement.execute(DROP_USERS_TABLE_DDL_SQL); // false
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }

        // 创建 users 表
        statement.execute(CREATE_USERS_TABLE_DDL_SQL); // false
    }

    public static void main(String[] args) throws Exception {
        DBInitialization.init();
    }
}
