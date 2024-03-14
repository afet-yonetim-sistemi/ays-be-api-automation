package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminsListPayload {
    Pagination pagination;

    public static AdminsListPayload generate(Pagination pagination) {
        AdminsListPayload adminsListPayload = new AdminsListPayload();
        adminsListPayload.setPagination(pagination);
        return adminsListPayload;
    }
}
