package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.common.util.AysRandomUtil;

import java.util.List;

@Getter
@Setter
public class RoleUpdatePayload {

    private String name;
    private List<String> permissionIds;

    public static RoleUpdatePayload generate() {
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol " + AysRandomUtil.generateAlphaSuffix());
        roleUpdatePayload.setPermissionIds(PermissionDataSource.findRandomPermissionIdsAsRoleManagementCategory());
        return roleUpdatePayload;
    }

}
