package org.ays.utility;

import lombok.extern.slf4j.Slf4j;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

}
