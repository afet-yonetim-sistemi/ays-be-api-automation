package org.ays.tests.database.aysInstitutionName;

import lombok.extern.slf4j.Slf4j;
import org.ays.utility.DatabaseUtility;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class AfetYonetimSistemi extends DatabaseUtility {

    private static int dbUserCount = 0;

    @BeforeClass
    public static void setUp() {
        DBConnection();
    }

    @Test(description = "Verify AYS user count")
    public void testAYSCount() {
        try {
            String query = DatabaseUtility.getUserCountQuery("Afet Yönetim Sistemi");

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                dbUserCount = resultSet.getInt("user_count");
                //System.out.println(dbUserCount);
            }

            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute test due to database error");
        }
    }

    @Test(description = "Verify AYS role count")
    public void testAYSRoleCount() {
        try {
            String query = DatabaseUtility.getRoleCountQuery("Afet Yönetim Sistemi");

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                dbUserCount = resultSet.getInt("ROLE_COUNT");

            }

            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute test due to database error");
        }
    }

    @AfterClass
    public static void tearDown() {
        DBConnectionClose();
    }

    public int getDbUserCount() {
        return dbUserCount;
    }
}
