package org.ays.auth.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.ays.common.datasource.AysDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PermissionDataSource {

    public static List<String> getPermissionsId() {
        String category = "ROLE_MANAGEMENT";

        List<Permission> permissions = fetchPermissionsFromDatabase();

        List<Permission> filteredPermissions = permissions.stream()
                .filter(permission -> category.equals(permission.getCategory()))
                .collect(Collectors.toList());

        Collections.shuffle(filteredPermissions);

        return filteredPermissions.stream()
                .limit(2)
                .map(Permission::getId)
                .collect(Collectors.toList());
    }

    private static List<Permission> fetchPermissionsFromDatabase() {

        String query = "SELECT ID, CATEGORY FROM AYS_PERMISSION";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Permission> permissions = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String category = resultSet.getString("CATEGORY");
                permissions.add(new Permission(id, category));
            }

            return permissions;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Getter
    @Setter
    static class Permission {
        private String id;
        private String category;

        public Permission(String id, String category) {
            this.id = id;
            this.category = category;
        }
    }

}
