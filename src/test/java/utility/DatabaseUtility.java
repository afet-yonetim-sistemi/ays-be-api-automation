package utility;

import java.sql.*;

public class DatabaseUtility {
    public Statement statement;
    private static String jdbcUrl = ConfigurationReader.getProperty("jdbcUrl");
    private static String username = ConfigurationReader.getProperty("dbusername");
    private static String password = ConfigurationReader.getProperty("dbpassword");
    private static Connection connection;
    public void connect() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserStatus(String newStatus) {
        String username=ConfigurationReader.getProperty("user4name");
        try {
            String sql = "UPDATE AYS_USER SET SUPPORT_STATUS = ? WHERE USERNAME = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newStatus);
            statement.setString(2, username);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update user status.");
        }
    }

    public void updateAssignments() {
        try {
            String userId = ConfigurationReader.getProperty("userID");
            String sql = "UPDATE AYS_ASSIGNMENT SET USER_ID = NULL, status = 'AVAILABLE' WHERE USER_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update assignments.");
        }
    }

}
