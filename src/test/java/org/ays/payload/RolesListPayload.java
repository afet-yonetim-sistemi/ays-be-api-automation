package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.RoleStatus;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.common.model.payload.AysPageable;

@Getter
@Setter
public class RolesListPayload {

    private AysPageable pageable;
    private RolesListFilter filter;

    public static RolesListPayload generate() {
        RolesListPayload rolesListPayload = new RolesListPayload();
        rolesListPayload.setPageable(AysPageable.generate(1, 10));
        return rolesListPayload;
    }

    public static RolesListPayload generateWithFilter(RoleCreatePayload roleCreatePayload) {
        RolesListPayload rolesListPayload = new RolesListPayload();
        rolesListPayload.setPageable(AysPageable.generate(1, 10));
        rolesListPayload.setFilter(RolesListFilter.generate(roleCreatePayload.getName(), RoleStatus.ACTIVE));
        return rolesListPayload;
    }

}
