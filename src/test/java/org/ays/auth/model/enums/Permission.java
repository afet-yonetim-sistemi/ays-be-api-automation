package org.ays.auth.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    APPLICATION_EVACUATION_UPDATE("application:evacuation:update"),
    APPLICATION_EVACUATION_DETAIL("application:evacuation:detail"),
    APPLICATION_EVACUATION_LIST("application:evacuation:list"),

    APPLICATION_REGISTRATION_DETAIL("application:registration:detail"),
    APPLICATION_REGISTRATION_LIST("application:registration:list"),
    APPLICATION_REGISTRATION_CREATE("application:registration:create"),
    APPLICATION_REGISTRATION_CONCLUDE("application:registration:conclude"),

    ROLE_LIST("role:list"),
    ROLE_DELETE("role:delete"),
    ROLE_CREATE("role:create"),
    ROLE_UPDATE("role:update"),
    ROLE_DETAIL("role:detail"),

    USER_LIST("user:list"),
    USER_DETAIL("user:detail"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),
    USER_UPDATE("user:update"),

    SUPER("super"),

    LANDING_PAGE("landing:page"),
    INSTITUTION_PAGE("institution:page");

    private final String permission;

}
