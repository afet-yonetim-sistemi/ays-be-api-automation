package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.RoleStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class RolesListFilter {

    private String name;
    private List<String> statuses;

    public static RolesListFilter generate(String name, RoleStatus statuses) {
        RolesListFilter rolesFilter = new RolesListFilter();
        rolesFilter.setName(name);
        rolesFilter.setStatuses(Collections.singletonList(statuses.toString()));
        return rolesFilter;
    }

}
