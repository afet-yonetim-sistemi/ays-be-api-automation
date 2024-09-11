package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.RoleStatus;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.common.model.payload.AysPageable;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class RoleListPayload {

    private AysPageable pageable;
    private Filter filter;

    public static RoleListPayload generate() {
        RoleListPayload roleListPayload = new RoleListPayload();
        roleListPayload.setPageable(AysPageable.generate(1, 10));
        return roleListPayload;
    }

    public static RoleListPayload generateWithFilter(RoleCreatePayload roleCreatePayload) {
        RoleListPayload roleListPayload = new RoleListPayload();
        roleListPayload.setPageable(AysPageable.generate(1, 10));
        roleListPayload.setFilter(Filter.generate(roleCreatePayload.getName(), RoleStatus.ACTIVE));
        return roleListPayload;
    }

    @Getter
    @Setter
    public static class Filter {

        private String name;
        private List<String> statuses;

        public static Filter generate(String name, RoleStatus statuses) {
            Filter rolesFilter = new Filter();
            rolesFilter.setName(name);
            rolesFilter.setStatuses(Collections.singletonList(statuses.toString()));
            return rolesFilter;
        }

    }


}
