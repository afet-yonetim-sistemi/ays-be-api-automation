package org.ays.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtility {
    private static final Logger logger = LogManager.getLogger(DatabaseUtility.class);
    private static final String jdbcUrl = ConfigurationReader.getProperty("jdbcUrl");
    private static final String username = ConfigurationReader.getProperty("DbUsername");
    private static final String password = ConfigurationReader.getProperty("DbPassword");
    protected static Statement statement;
    static Connection connection;

    public static void DBConnection() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            logger.error("Error establishing database connection: {}", e.getMessage());
        }
    }

    public static void DBConnectionClose() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection: {}", e.getMessage());
        }
    }

}
