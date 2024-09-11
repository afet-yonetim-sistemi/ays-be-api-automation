package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesListPayload {

    private Pageable pageable;
    private RolesListFilter filter;

    public static RolesListPayload generate() {
        RolesListPayload rolesListPayload = new RolesListPayload();
        rolesListPayload.setPageable(Pageable.generate(1, 10));
        return rolesListPayload;
    }

    public static RolesListPayload generateWithFilter(RoleCreatePayload roleCreatePayload) {
        RolesListPayload rolesListPayload = new RolesListPayload();
        rolesListPayload.setPageable(Pageable.generate(1, 10));
        rolesListPayload.setFilter(RolesListFilter.generate(roleCreatePayload.getName(), RoleStatus.ACTIVE));
        return rolesListPayload;
    }

}
