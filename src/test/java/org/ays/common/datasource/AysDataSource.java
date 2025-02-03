package org.ays.common.datasource;

import org.ays.common.util.AysConfigurationProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AysDataSource {

    private AysDataSource() {
    }

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                AysConfigurationProperty.Database.URL,
                AysConfigurationProperty.Database.USERNAME,
                AysConfigurationProperty.Database.PASSWORD
        );
    }

    public static void refreshDatabase() {
        try (Connection connection = AysDataSource.createConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("COMMIT;");

            System.out.println("Database refreshed successfully.");

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to refresh database", exception);
        }
    }


}
