package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleUpdatePayload {

    private String name;
    private List<String> permissionIds;

}
