package org.ays.tests.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ays.utility.DatabaseUtility;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvalidTokensTest extends DatabaseUtility {
    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeMethod
    public void setup() {
        DBConnection();
    }

    @Test(description = "Validate the removal of expired tokens from the invalid token table")
    public void validateExpiredTokenDeletion() {
        String query = "SELECT IF(COUNT(*) > 0, 'true', 'false') AS IS_INVALID_TOKEN_EXIST " +
                "FROM AYS_INVALID_TOKEN " +
                "WHERE (CREATED_AT + INTERVAL (SELECT DEFINITION FROM AYS_PARAMETER WHERE NAME = 'AUTH_REFRESH_TOKEN_EXPIRE_DAY') DAY + INTERVAL 1 DAY) < NOW()";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                String result = resultSet.getString("IS_INVALID_TOKEN_EXIST");
                if (result.equals("false")) {
                    Assert.assertTrue(true, "No expired tokens found");
                } else if (result.equals("true")) {
                    Assert.fail("There are expired tokens");
                }
            } else {
                Assert.fail("No result returned from the query");
            }
        } catch (SQLException e) {
            logger.error("Failed to execute the validateExpiredTokenDeletion test: {}", e.getMessage());
            throw new RuntimeException("Failed to execute the validateExpiredTokenDeletion test.");
        }
    }

    @AfterMethod
    public void cleanup() {
        DBConnectionClose();
    }


}
