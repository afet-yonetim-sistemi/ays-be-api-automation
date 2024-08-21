package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleUpdatePayload {

    private String name;
    private List<String> permissionIds;

    public static RoleUpdatePayload generate(RoleCreatePayload roleCreatePayload) {
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(roleCreatePayload.getName());
        roleUpdatePayload.setPermissionIds(roleCreatePayload.getPermissionIds());
        return roleUpdatePayload;
    }

    public static RoleUpdatePayload generate() {
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(RoleCreatePayload.generate().getName());
        roleUpdatePayload.setPermissionIds(RoleCreatePayload.generate().getPermissionIds());
        return roleUpdatePayload;
    }
}
