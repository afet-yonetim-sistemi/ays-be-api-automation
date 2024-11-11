package org.ays.auth.datasource;

import lombok.experimental.UtilityClass;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RoleDataSource {

    public static int findRoleCountByInstitutionId(String institutionId) {
        String query = "SELECT COUNT(*) AS ROLE_COUNT " +
                "FROM AYS_ROLE ROLE " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROLE.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.ID = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);
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

    public static String findLastCreatedRoleIdByInstitutionId(String institutionId) {
        String query = "SELECT ID FROM AYS_ROLE WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ID");
                }
            }

            throw new RuntimeException("No roles found for the given institution ID");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String findLastCreatedRoleNameByInstitutionId(String institutionId) {

        String query = "SELECT NAME FROM AYS_ROLE WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("NAME");
                }
            }

            throw new RuntimeException("No roles found for the given institution ID");

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String findLastCreatedRoleStatusByInstitutionId(String institutionId) {

        String query = "SELECT STATUS FROM AYS_ROLE WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("STATUS");
                }
            }

            throw new RuntimeException("No roles found for the given institution ID");

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static List<String> getPermissionIdsFromLastCreatedRole(String roleId) {

        String query = "SELECT PERMISSION_ID FROM AYS_ROLE_PERMISSION_RELATION WHERE ROLE_ID = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, roleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> permissions = new ArrayList<>();

                while (resultSet.next()) {
                    String permissionId = resultSet.getString("PERMISSION_ID");
                    permissions.add(permissionId);
                }

                return permissions;
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error fetching permission IDs for role: " + roleId, exception);
        }
    }

}
