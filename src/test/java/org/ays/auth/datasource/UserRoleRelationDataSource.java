package org.ays.auth.datasource;

import lombok.experimental.UtilityClass;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserRoleRelationDataSource {

    public static List<String> findAllRoleIdsByUserId(String userId) {
        String query = "SELECT ROLE_ID FROM AYS_USER_ROLE_RELATION WHERE USER_ID = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> roleIds = new ArrayList<>();

                while (resultSet.next()) {
                    String roleId = resultSet.getString("ROLE_ID");
                    roleIds.add(roleId);
                }

                return roleIds;
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error fetching role IDs for user: " + userId, exception);
        }
    }

}
