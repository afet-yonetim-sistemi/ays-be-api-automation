package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.common.util.AysRandomUtil;

import java.util.List;

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

}
