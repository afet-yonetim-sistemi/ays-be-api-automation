package org.ays.common.datasource;

import org.ays.common.util.AysConfigurationProperty;

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
