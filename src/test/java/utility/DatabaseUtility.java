package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {
    private static Connection connection;
    private static String jdbcUrl = ConfigurationReader.getProperty("jdbcUrl");
    private static String username = ConfigurationReader.getProperty("dbusername");
    private static String password = ConfigurationReader.getProperty("dbpassword");

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(jdbcUrl, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
