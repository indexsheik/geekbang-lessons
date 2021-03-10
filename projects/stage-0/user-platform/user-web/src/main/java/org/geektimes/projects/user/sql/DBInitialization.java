package org.geektimes.projects.user.sql;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * db 初始化
 *
 * @Author: Xiao Xuezhi
 * @Date: 2021/3/3 23:06
 * @Version: 1.0
 */
public class DBInitialization {

    private static Logger logger = Logger.getLogger(DBInitialization.class.getName());

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    @Resource(name = "bean/DBConnectionManager")
    private DBConnectionManager dbConnectionManager;

    @PostConstruct
    public void init() {
        Connection connection = dbConnectionManager.getConnection();

        try {
            Statement statement = connection.createStatement();
            // 创建 users 表
            statement.execute(CREATE_USERS_TABLE_DDL_SQL); // false
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
