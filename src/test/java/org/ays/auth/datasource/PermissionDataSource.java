package org.ays.auth.datasource;

import lombok.experimental.UtilityClass;
import org.ays.auth.model.enums.PermissionCategory;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class PermissionDataSource {

    public static List<String> findRandomPermissionIdsAsRoleManagementCategory() {
        List<String> permissionIds = findPermissionIdsByCategory(PermissionCategory.ROLE_MANAGEMENT);
        Collections.shuffle(permissionIds);
        return permissionIds.stream()
                .limit(2)
                .toList();
    }

    private static List<String> findPermissionIdsByCategory(PermissionCategory permissionCategory) {

        String query = "SELECT ID FROM AYS_PERMISSION WHERE CATEGORY = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, permissionCategory.name());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                List<String> permissions = new ArrayList<>();
                while (resultSet.next()) {
                    String id = resultSet.getString("ID");
                    permissions.add(id);
                }
                return permissions;
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
