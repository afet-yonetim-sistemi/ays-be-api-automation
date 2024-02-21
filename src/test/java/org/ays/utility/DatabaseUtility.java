package org.ays.utility;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DatabaseUtility {

    protected static Statement statement;
    private static Connection connection;

    private static final String jdbcUrl = AysConfigurationProperty.Database.URL;
    private static final String username = AysConfigurationProperty.Database.USERNAME;
    private static final String password = AysConfigurationProperty.Database.PASSWORD;

    public static void DBConnection() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            log.error("Error establishing database connection: {}", e.getMessage());
        }
    }

    public static void DBConnectionClose() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Error closing database connection: {}", e.getMessage());
        }
    }

}
