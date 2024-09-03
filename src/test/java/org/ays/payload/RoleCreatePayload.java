package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;
import org.ays.utility.DatabaseUtility;

import java.util.List;

@Getter
@Setter
public class RoleCreatePayload {

    private String name;
    private List<String> permissionIds;

    public static RoleCreatePayload generate() {
        RoleCreatePayload roleCreatePayload = new RoleCreatePayload();
        roleCreatePayload.setName(AysRandomUtil.generateFirstName() + " Rol " + AysRandomUtil.generateAlphaSuffix());
        roleCreatePayload.setPermissionIds(DatabaseUtility.getPermissionsId());
        return roleCreatePayload;
    }

}
