package org.example.utils.DbConnection;

import com.zaxxer.hikari.HikariDataSource;
import org.example.utils.ResourceReader;

import javax.sql.DataSource;

public class ConnectionPool {

    private static final String URL = ResourceReader.readApplicationProperties("db.url");
    private static final String USERNAME = ResourceReader.readApplicationProperties("db.username");
    private static final String PASSWORD = ResourceReader.readApplicationProperties("db.password");
    private static final HikariDataSource dataSource;

    static {
        dataSource = new HikariDataSource();
        dataSource.setDriverClassName(ResourceReader.readApplicationProperties("driver.class.name"));
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMinimumIdle(2);
        dataSource.setMaximumPoolSize(4);
    }

    private ConnectionPool() {
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
