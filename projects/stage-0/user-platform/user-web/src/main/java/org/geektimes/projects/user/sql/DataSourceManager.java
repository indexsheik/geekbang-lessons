package org.geektimes.projects.user.sql;

import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * TODO
 *
 * @Author: Xiao Xuezhi
 * @Date: 2021/3/10 23:30
 * @Version: 1.0
 */
public class DataSourceManager {

    private DataSource dataSource;

    private String databaseName;

    private String createDatabase;

    @PostConstruct
    public void init() {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(databaseName);
        dataSource.setCreateDatabase(createDatabase);
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setCreateDatabase(String createDatabase) {
        this.createDatabase = createDatabase;
    }
}
