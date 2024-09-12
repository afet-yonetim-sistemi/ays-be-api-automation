package org.ays.auth.datasource;

import lombok.experimental.UtilityClass;
import org.ays.common.datasource.AysDataSource;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.payload.UsersFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@UtilityClass
public class UserDataSource {

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

    public static String getLatestCreatedUserId(String institutionId) {
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

}
