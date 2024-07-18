package org.ays.utility;

import lombok.extern.slf4j.Slf4j;
import org.ays.payload.PhoneNumber;
import org.ays.payload.UsersFilter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
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

    public static String getLatestReferenceNumber() {
        String query = "SELECT REFERENCE_NUMBER FROM AYS_EMERGENCY_EVACUATION_APPLICATION ORDER BY CREATED_AT DESC LIMIT 1";
        String referenceNumber = null;

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    referenceNumber = resultSet.getString("REFERENCE_NUMBER");
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching latest reference number: {}", e.getMessage());
        } finally {
            DBConnectionClose();
        }

        return referenceNumber;
    }

    public static UsersFilter fetchFirstUserData() {
        UsersFilter usersFilter = new UsersFilter();
        String query = "SELECT USER.FIRST_NAME, USER.LAST_NAME,\n" +
                "USER.COUNTRY_CODE, USER.LINE_NUMBER, USER.CITY, USER.STATUS\n" +
                "FROM AYS_USER USER\n" +
                "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID\n" +
                "LIMIT 1";
        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {

                usersFilter.setFirstName(resultSet.getString("FIRST_NAME"));
                usersFilter.setLastName(resultSet.getString("LAST_NAME"));
                usersFilter.setCity(resultSet.getString("CITY"));
                usersFilter.setStatuses(Collections.singletonList(resultSet.getString("STATUS")));

                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setCountryCode(resultSet.getString("COUNTRY_CODE"));
                phoneNumber.setLineNumber(resultSet.getString("LINE_NUMBER"));

                usersFilter.setPhoneNumber(phoneNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user data due to database error");
        } finally {
            DBConnectionClose();
        }
        return usersFilter;
    }

    public static String getUserCountQuery(String institutionName) {
        return "SELECT COUNT(DISTINCT USER.EMAIL_ADDRESS) AS user_count " +
                "FROM AYS_USER USER " +
                "JOIN ays.AYS_USER_ROLE_RELATION USER_ROLE_RELATION ON USER.ID = USER_ROLE_RELATION.USER_ID " +
                "JOIN AYS_ROLE ROLE ON USER_ROLE_RELATION.ROLE_ID = ROLE.ID " +
                "JOIN AYS_ROLE_PERMISSION_RELATION ON ROLE.ID = AYS_ROLE_PERMISSION_RELATION.ROLE_ID " +
                "JOIN AYS_PERMISSION PERMISSION ON AYS_ROLE_PERMISSION_RELATION.PERMISSION_ID = PERMISSION.ID " +
                "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = '" + institutionName + "'";
    }
}
