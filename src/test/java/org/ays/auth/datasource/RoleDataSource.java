package org.ays.auth.datasource;

import lombok.experimental.UtilityClass;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class RoleDataSource {

    public static int findRoleCountByInstitutionName(String institutionName) {
        String query = "SELECT COUNT(*) AS ROLE_COUNT " +
                "FROM AYS_ROLE ROLE " +
                "JOIN AYS_INSTITUTION INSTITUTION ON ROLE.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.NAME = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionName);
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

    public static String findRoleIdByInstitutionId(String institutionId) {

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

}
