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
            String query = "SELECT COUNT(DISTINCT USER.EMAIL_ADDRESS) AS user_count " +
                    "FROM AYS_USER USER " +
                    "JOIN ays.AYS_USER_ROLE_RELATION USER_ROLE_RELATION ON USER.ID = USER_ROLE_RELATION.USER_ID " +
                    "JOIN AYS_ROLE ROLE ON USER_ROLE_RELATION.ROLE_ID = ROLE.ID " +
                    "JOIN AYS_ROLE_PERMISSION_RELATION ON ROLE.ID = AYS_ROLE_PERMISSION_RELATION.ROLE_ID " +
                    "JOIN AYS_PERMISSION PERMISSION ON AYS_ROLE_PERMISSION_RELATION.PERMISSION_ID = PERMISSION.ID " +
                    "JOIN AYS_INSTITUTION INSTITUTION ON USER.INSTITUTION_ID = INSTITUTION.ID " +
                    "WHERE INSTITUTION.NAME = 'Afet Yönetim Sistemi'";

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

    @AfterClass
    public static void tearDown() {
        DBConnectionClose();
    }

    public int getDbUserCount() {
        return dbUserCount;
    }
}
