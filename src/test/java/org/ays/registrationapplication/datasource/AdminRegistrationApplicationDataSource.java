package org.ays.registrationapplication.datasource;

import lombok.experimental.UtilityClass;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class AdminRegistrationApplicationDataSource {

    public static String findLastCreatedId() {
        String query = "SELECT ID FROM AYS_ADMIN_REGISTRATION_APPLICATION ORDER BY CREATED_AT DESC LIMIT 1";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString("ID");
            }

            return null;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
