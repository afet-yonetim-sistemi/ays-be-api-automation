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

    public static List<String> findAllPermissionNames() {

        String query = "SELECT NAME FROM AYS_PERMISSION";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<String> permissions = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                permissions.add(name);
            }
            return permissions;

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static List<String> findAllPermissionIds() {

        String query = "SELECT ID FROM AYS_PERMISSION";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<String> permissions = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                permissions.add(id);
            }
            return permissions;

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static List<String> findAllPermissionNamesByIsSuper(boolean isSuper) {

        String query = "SELECT NAME FROM AYS_PERMISSION WHERE IS_SUPER = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setBoolean(1, isSuper);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                List<String> permissions = new ArrayList<>();
                while (resultSet.next()) {
                    String name = resultSet.getString("NAME");
                    permissions.add(name);
                }
                return permissions;
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String findPermissionIdByName(String permissionName) {
        String query = "SELECT ID FROM AYS_PERMISSION WHERE NAME = ?";

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, permissionName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ID");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error fetching permission ID for name: " + permissionName, exception);
        }

        return null;
    }

    public static List<String> findPermissionIdsByPermissionList(List<String> permissionNames) {
        if (permissionNames == null) {
            throw new IllegalArgumentException("Permission names list cannot be null.");
        }

        if (permissionNames.isEmpty()) {
            System.out.println("Permission names list is empty. Returning an empty list.");
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(permissionNames.size(), "?"));
        String query = "SELECT ID FROM AYS_PERMISSION WHERE NAME IN (" + placeholders + ")";

        List<String> permissionIds = new ArrayList<>();

        try (Connection connection = AysDataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < permissionNames.size(); i++) {
                preparedStatement.setString(i + 1, permissionNames.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    permissionIds.add(resultSet.getString("ID"));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error fetching permission IDs for names", exception);
        }

        return permissionIds;
    }


}
