package org.ays.auth.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum PermissionCategory {

    ROLE_MANAGEMENT(
            Permission.ROLE_LIST,
            Permission.ROLE_CREATE,
            Permission.ROLE_UPDATE,
            Permission.ROLE_DELETE,
            Permission.ROLE_DETAIL
    ),
    PAGE(
            Permission.INSTITUTION_PAGE,
            Permission.LANDING_PAGE
    ),
    USER_MANAGEMENT(
            Permission.USER_LIST,
            Permission.USER_CREATE,
            Permission.USER_UPDATE,
            Permission.USER_DELETE,
            Permission.USER_DETAIL
    ),
    EVACUATION_APPLICATION_MANAGEMENT(
            Permission.APPLICATION_EVACUATION_LIST,
            Permission.APPLICATION_EVACUATION_UPDATE,
            Permission.APPLICATION_EVACUATION_DETAIL
    ),
    REGISTRATION_APPLICATION_MANAGEMENT(
            Permission.APPLICATION_REGISTRATION_LIST,
            Permission.APPLICATION_REGISTRATION_CREATE,
            Permission.APPLICATION_REGISTRATION_DETAIL,
            Permission.APPLICATION_REGISTRATION_CONCLUDE
    ),
    SUPER_ADMIN(
            Permission.SUPER
    );

    private final Permission[] permissions;

    PermissionCategory(Permission... permissions) {
        this.permissions = permissions;
    }

    public List<String> getPermissionNames() {
        return Arrays.stream(this.permissions)
                .map(Permission::getPermission)
                .collect(Collectors.toList());
    }


}
