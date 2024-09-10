package org.ays.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

}
