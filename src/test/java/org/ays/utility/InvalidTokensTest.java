package org.ays.utility;

import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class InvalidTokensTest {

    @Test(description = "Validate the removal of expired tokens from the invalid token table")
    public void validateExpiredTokenDeletion() {
        String query = "SELECT IF(COUNT(*) > 0, 'true', 'false') AS IS_INVALID_TOKEN_EXIST " +
                "FROM AYS_INVALID_TOKEN " +
                "WHERE (CREATED_AT + INTERVAL (SELECT DEFINITION FROM AYS_PARAMETER WHERE NAME = 'AUTH_REFRESH_TOKEN_EXPIRE_DAY') DAY + INTERVAL 1 DAY) < NOW()";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                String result = resultSet.getString("IS_INVALID_TOKEN_EXIST");
                if (result.equals("false")) {
                    Assert.assertTrue(true, "No expired tokens found");
                    return;
                }

                if (result.equals("true")) {
                    Assert.fail("There are expired tokens");
                }

                return;
            }

            Assert.fail("No result returned from the query");
        } catch (SQLException e) {
            log.error("Failed to execute the validateExpiredTokenDeletion test: {}", e.getMessage());
            throw new RuntimeException("Failed to execute the validateExpiredTokenDeletion test.");
        }

    }

}
