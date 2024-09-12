package org.ays.emergencyapplication.datasource;

import lombok.experimental.UtilityClass;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class EmergencyEvacuationApplicationDataSource {

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

}
