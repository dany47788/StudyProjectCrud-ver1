package org.example.utils.DbConnection;

import org.example.utils.ResourceReader;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtils {


    private static final String URL = ResourceReader.readApplicationProperties("db.url");
    private static final String USERNAME = ResourceReader.readApplicationProperties("db.username");
    private static final String PASSWORD = ResourceReader.readApplicationProperties("db.password");

    private JdbcUtils() {
    }

    public static Connection getConnection() {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
