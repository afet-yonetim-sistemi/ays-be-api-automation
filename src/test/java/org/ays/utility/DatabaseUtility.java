package org.ays.utility;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ays.payload.PhoneNumber;
import org.ays.payload.UsersFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DatabaseUtility {

    public static List<String> getColumNameFromTableWhereValueIs(String columnName, String tableName) {
        return getColumNameFromTableWhereValueIs(columnName, tableName, null, null);
    }

    public static List<String> getColumNameFromTableWhereValueIs(String columnName, String tableName, String filterName, Object filterValue) {

        String query = "SELECT " + columnName + " FROM " + tableName;

        if (filterName != null && filterValue != null) {
            query += " WHERE " + filterName + " = ?";
        }

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (filterName != null && filterValue != null) {
                preparedStatement.setObject(1, filterValue);
            }

            List<String> valuesInColumn = new ArrayList<>();
            while (resultSet.next()) {
                valuesInColumn.add(resultSet.getString(columnName));
            }

            return valuesInColumn;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String getLatestReferenceNumber() {
        String query = "SELECT REFERENCE_NUMBER FROM AYS_EMERGENCY_EVACUATION_APPLICATION ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString("REFERENCE_NUMBER");
            }

            return null;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static UsersFilter fetchFirstUserData() {
        String query = "SELECT USER.FIRST_NAME, USER.LAST_NAME,\n" +
                "USER.COUNTRY_CODE, USER.LINE_NUMBER, USER.CITY, USER.STATUS\n" +
                "FROM AYS_USER USER\n" +
                "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID\n" +
                "LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            UsersFilter usersFilter = new UsersFilter();

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

            return usersFilter;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String fetchFirstUserEmailAddress() {

        String query = "SELECT EMAIL_ADDRESS FROM AYS_USER LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString("EMAIL_ADDRESS");
            }

            return null;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String getRoleId(String institutionId) {
        String query = "SELECT ID FROM AYS_USER WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ID");
                }
            }

            return null;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
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

    private static List<Permission> fetchPermissionsFromDatabase() {

        String query = "SELECT ID, CATEGORY FROM AYS_PERMISSION";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Permission> permissions = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String category = resultSet.getString("CATEGORY");
                permissions.add(new Permission(id, category));
            }

            return permissions;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
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
        String query = "SELECT COUNT(*) AS user_count\n" +
                "FROM AYS_USER USER\n" +
                "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID\n" +
                "WHERE INSTITUTION.NAME = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foundationName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_count");
                }
            }

            return 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }

    public static int verifyRoleCountForFoundation(String foundationName) {
        String query = "SELECT COUNT(ROLE.NAME) AS ROLE_COUNT " +
                "FROM AYS_ROLE ROLE " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROLE.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foundationName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ROLE_COUNT");
                }
            }

            return 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }

    public static String getLastCreatedRoleId() {
        String query = "SELECT ID FROM AYS_ROLE ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString("ID");
            }

            throw new RuntimeException("No roles found for the given institution ID");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String getLatestCreatedUserId(String institutionId) {
        String query = "SELECT ID FROM AYS_USER WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";
        String userId = null;

        try {
            if (connection == null || connection.isClosed()) {
                DBConnection();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, institutionId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userId = resultSet.getString("ID");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching user ID: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch user ID due to database error", e);
        } finally {
            DBConnectionClose();
        }

        if (userId == null) {
            throw new RuntimeException("No user found for the given institution ID");
        }

        return userId;
    }


    public static String getRoleIdForInstitution(String institutionName) {
        String query = "SELECT ROL.ID " +
                "FROM AYS_ROLE ROL " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROL.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = ? " +
                "LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ID");
                }
            }

            return "";
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    // findLastDeletedRoleIdByInstitutionName
    public static String getDeletedRoleIdForInstitution(String institutionName) {
        String query = "SELECT ROL.ID " +
                "FROM AYS_ROLE ROL " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROL.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = ? " +
                "AND ROL.STATUS = 'DELETED' " +
                "LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ID");
                }
            }

            return "";
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }

}
