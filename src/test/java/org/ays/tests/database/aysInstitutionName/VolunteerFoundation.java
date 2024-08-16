package org.ays.tests.database.aysInstitutionName;

import lombok.extern.slf4j.Slf4j;
import org.ays.utility.DatabaseUtility;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class VolunteerFoundation extends DatabaseUtility {

    private static int dbUserCount = 0;

    @BeforeClass
    public static void setUp() {
        DBConnection();
    }

    @Test(description = "Verify Volunteer Foundation user count")
    public void testVolunteerFoundationCount() {
        try {
            String query = DatabaseUtility.getUserCountQuery("Volunteer Foundation");

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

    @Test(description = "Verify Volunteer Foundation role count")
    public void testVolunteerFoundationRoleCount() {
        try {
            String query = DatabaseUtility.getRoleCountQuery("Volunteer Foundation");

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
