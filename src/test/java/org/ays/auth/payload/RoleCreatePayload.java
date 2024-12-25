package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.model.enums.Permission;
import org.ays.auth.model.enums.PermissionCategory;
import org.ays.common.util.AysRandomUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RoleCreatePayload {

    private String name;
    private List<String> permissionIds;

    public static RoleCreatePayload generate() {
        RoleCreatePayload roleCreatePayload = new RoleCreatePayload();
        roleCreatePayload.setName(AysRandomUtil.generateFirstName() + " Rol " + AysRandomUtil.generateAlphaSuffix());
        roleCreatePayload.setPermissionIds(PermissionDataSource.findRandomPermissionIdsAsRoleManagementCategory());
        return roleCreatePayload;
    }

    public static RoleCreatePayload generateRoleWithPermissions(Object permissions) {
        RoleCreatePayload roleCreatePayload = new RoleCreatePayload();
        roleCreatePayload.setName(AysRandomUtil.generateFirstName() + " Rol " + AysRandomUtil.generateAlphaSuffix());

        List<String> permissionIds;

        if (permissions instanceof Permission) {
            permissionIds = Arrays.asList(PermissionDataSource.findPermissionIdByPermissionName(((Permission) permissions)));
        } else if (permissions instanceof List) {
            permissionIds = ((List<Permission>) permissions).stream()
                    .map(PermissionDataSource::findPermissionIdByPermissionName)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Permissions should be of type Permission or List<Permission>");
        }

        roleCreatePayload.setPermissionIds(permissionIds);

        return roleCreatePayload;
    }

    public static RoleCreatePayload generateRoleWithCategoryPermissions(Object categories) {
        RoleCreatePayload roleCreatePayload = new RoleCreatePayload();
        roleCreatePayload.setName(AysRandomUtil.generateFirstName() + " Rol " + AysRandomUtil.generateAlphaSuffix());

        List<String> permissionIds;

        if (categories instanceof PermissionCategory) {
            permissionIds = Arrays.stream(((PermissionCategory) categories).getPermissions())
                    .map(PermissionDataSource::findPermissionIdByPermissionName)
                    .collect(Collectors.toList());
        } else if (categories instanceof List) {
            permissionIds = ((List<PermissionCategory>) categories).stream()
                    .flatMap(category -> Arrays.stream(category.getPermissions()))
                    .map(PermissionDataSource::findPermissionIdByPermissionName)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Categories should be of type PermissionCategory or List<PermissionCategory>");
        }

        roleCreatePayload.setPermissionIds(permissionIds);

        return roleCreatePayload;
    }

}
