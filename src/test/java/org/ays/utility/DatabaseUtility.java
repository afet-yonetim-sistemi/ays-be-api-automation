package org.ays.utility;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ays.payload.PhoneNumber;
import org.ays.payload.UsersFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public static String getUserCountQuery() {
        return "SELECT COUNT(*) AS user_count\n" +
                "FROM AYS_USER USER\n" +
                "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID\n" +
                "WHERE INSTITUTION.NAME = ?";
    }

    public static String fetchFirstUserEmailAddress() {

        String query = "SELECT EMAIL_ADDRESS FROM AYS_USER LIMIT 1";
        String emailAddress = null;

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                emailAddress = resultSet.getString("EMAIL_ADDRESS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user data due to database error");
        } finally {
            DBConnectionClose();
        }
        return emailAddress;
    }

    public static String getRoleId(String institutionId) {
        String query = "SELECT ID FROM AYS_USER WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";
        String roleId = null;

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, institutionId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        roleId = resultSet.getString("ID");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching role ID: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch role ID due to database error");
        } finally {
            DBConnectionClose();
        }

        return roleId;

    }

    public static String getRoleCountQuery() {
        return "SELECT COUNT(ROLE.NAME) AS ROLE_COUNT " +
                "FROM AYS_ROLE ROLE " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROLE.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = ?";
    }

    public static List<Permission> fetchPermissionsFromDatabase() {
        List<Permission> permissions = new ArrayList<>();
        String query = "SELECT ID, CATEGORY FROM AYS_PERMISSION";

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String category = resultSet.getString("CATEGORY");
                permissions.add(new Permission(id, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch permissions due to database error");
        } finally {
            DBConnectionClose();
        }

        return permissions;
    }

    public static List<String> getPermissionsId() {
        String category = "ROLE_MANAGEMENT";

        List<Permission> permissions = fetchPermissionsFromDatabase();

        List<Permission> filteredPermissions = permissions.stream()
                .filter(permission -> category.equals(permission.getCategory()))
                .collect(Collectors.toList());

        Collections.shuffle(filteredPermissions);

        return filteredPermissions.stream()
                .limit(2)
                .map(Permission::getId)
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    static class Permission {
        private String id;
        private String category;

        public Permission(String id, String category) {
            this.id = id;
            this.category = category;
        }
    }

    public static int verifyUserCountForFoundation(String foundationName) {
        String query = DatabaseUtility.getUserCountQuery();
        int dbUserCount = 0;

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, foundationName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        dbUserCount = resultSet.getInt("user_count");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error verifying user count for foundation: ", e);
            throw new RuntimeException("Failed to verify user count due to database error", e);
        } finally {
            DBConnectionClose();
        }

        return dbUserCount;
    }

    public static int verifyRoleCountForFoundation(String foundationName) {
        String query = DatabaseUtility.getRoleCountQuery();
        int dbRoleCount = 0;

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, foundationName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        dbRoleCount = resultSet.getInt("ROLE_COUNT");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error verifying role count for foundation: ", e);
            throw new RuntimeException("Failed to verify role count due to database error", e);
        } finally {
            DBConnectionClose();
        }

        return dbRoleCount;
    }

    public static String getLastCreatedRoleId() {
        String query = "SELECT ID FROM AYS_ROLE ORDER BY CREATED_AT DESC LIMIT 1";
        String roleId = null;

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        roleId = resultSet.getString("ID");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching role ID: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch role ID due to database error", e);
        } finally {
            DBConnectionClose();
        }

        if (roleId == null) {
            throw new RuntimeException("No roles found for the given institution ID");
        }

        return roleId;
    }

    public static String getRoleIdForInstitution(String institutionName) {
        String query = "SELECT ROL.ID " +
                "FROM AYS_ROLE ROL " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROL.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = ? " +
                "LIMIT 1";
        String roleId = "";

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, institutionName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        roleId = resultSet.getString("ID");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error retrieving role ID for institution: ", e);
            throw new RuntimeException("Failed to retrieve role ID due to database error", e);
        } finally {
            DBConnectionClose();
        }

        return roleId;
    }

    public static String getDeletedRoleIdForInstitution(String institutionName) {
        String query = "SELECT ROL.ID " +
                "FROM AYS_ROLE ROL " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROL.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = ? AND ROL.STATUS = 'DELETED' " +
                "LIMIT 1";
        String roleId = "";

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, institutionName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        roleId = resultSet.getString("ID");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error retrieving deleted role ID for institution: ", e);
            throw new RuntimeException("Failed to retrieve deleted role ID due to database error", e);
        } finally {
            DBConnectionClose();
        }

        return roleId;
    }


}
