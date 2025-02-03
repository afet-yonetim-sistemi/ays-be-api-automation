package org.ays.auth.datasource;

import lombok.experimental.UtilityClass;
import org.ays.auth.model.entity.UserEntity;
import org.ays.common.datasource.AysDataSource;
import org.ays.common.model.payload.AysPhoneNumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class UserDataSource {

    public static UserEntity findAnyUser() {
        String query = "SELECT USER.FIRST_NAME, USER.LAST_NAME, USER.EMAIL_ADDRESS, " +
                "USER.COUNTRY_CODE, USER.LINE_NUMBER, USER.CITY, USER.STATUS " +
                "FROM AYS_USER USER " +
                "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID " +
                "LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            UserEntity usersFilter = new UserEntity();

            if (resultSet.next()) {

                usersFilter.setFirstName(resultSet.getString("FIRST_NAME"));
                usersFilter.setLastName(resultSet.getString("LAST_NAME"));
                usersFilter.setEmailAddress(resultSet.getString("EMAIL_ADDRESS"));
                usersFilter.setCity(resultSet.getString("CITY"));
                usersFilter.setStatuses(Collections.singletonList(resultSet.getString("STATUS")));

                AysPhoneNumber phoneNumber = new AysPhoneNumber();
                phoneNumber.setCountryCode(resultSet.getString("COUNTRY_CODE"));
                phoneNumber.setLineNumber(resultSet.getString("LINE_NUMBER"));

                usersFilter.setPhoneNumber(phoneNumber);
            }

            return usersFilter;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String findIdByPhoneNumber(AysPhoneNumber phoneNumber) {

        String query = "SELECT ID FROM AYS_USER WHERE COUNTRY_CODE = ? AND LINE_NUMBER = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNumber.getCountryCode());
            preparedStatement.setString(2, phoneNumber.getLineNumber());
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

    public static String findAnyEmailAddress() {

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

    public static int findUserCountByInstitutionId(String institutionId) {
        String query = "SELECT COUNT(*) AS USER_COUNT " +
                "FROM AYS_USER USER " +
                "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID " +
                "WHERE INSTITUTION.ID = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("USER_COUNT");
                }
            }

            return 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }

    public static String findLastCreatedUserIdByInstitutionId(String institutionId) {
        String query = "SELECT ID FROM AYS_USER WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ID");
                }
            }

            throw new RuntimeException("No user found for the given institution ID");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String findLastCreatedUserStatusByInstitutionId(String institutionId) {
        String query = "SELECT STATUS FROM AYS_USER WHERE INSTITUTION_ID = ? ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, institutionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("STATUS");
                }
            }

            throw new RuntimeException("No user found for the given institution ID");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static List<String> findAllRoleIdsById(String id) {
        String query = "SELECT ROLE_ID FROM AYS_USER_ROLE_RELATION WHERE USER_ID = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> roleIds = new ArrayList<>();

                while (resultSet.next()) {
                    String roleId = resultSet.getString("ROLE_ID");
                    roleIds.add(roleId);
                }

                return roleIds;
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error fetching role IDs for user: " + id, exception);
        }
    }

    public static String findPasswordIdByInstitutionId(String institutionId) {
        String userId = findLastCreatedUserIdByInstitutionId(institutionId);

        String query = "SELECT ID FROM AYS_USER_PASSWORD WHERE USER_ID = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ID");
                }
            }

            throw new RuntimeException("No password id found for the given institution ID");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
