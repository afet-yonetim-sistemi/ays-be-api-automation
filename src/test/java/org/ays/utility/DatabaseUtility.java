package org.ays.utility;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getColumNameFromTableWhereValueIs(String columnName, String tableName) {
        return getColumNameFromTableWhereValueIs(columnName, tableName, null, null);
    }

    // Overloaded method to fetch column values from a table with a WHERE clause
    public static List<String> getColumNameFromTableWhereValueIs(String columnName, String tableName, String filterName, Object filterValue) {
        List<String> valuesInColumn = new ArrayList<>();

        String query = "SELECT " + columnName + " FROM " + tableName;

        if (filterName != null && filterValue != null) {
            query += " WHERE " + filterName + " = ?";
        }

        try {
            // Ensure the connection is established
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                if (filterName != null && filterValue != null) {
                    preparedStatement.setObject(1, filterValue);
                }
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        valuesInColumn.add(resultSet.getString(columnName));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valuesInColumn;
    }
}
