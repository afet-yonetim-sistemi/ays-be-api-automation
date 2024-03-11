package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminsPayload {
    Pagination pagination;

    public static AdminsPayload generate(Pagination pagination) {
        AdminsPayload adminsPayload = new AdminsPayload();
        adminsPayload.setPagination(pagination);
        return adminsPayload;
    }
}
