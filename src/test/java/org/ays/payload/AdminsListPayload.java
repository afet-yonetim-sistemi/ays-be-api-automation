package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminsListPayload {
    Pageable pageable;

    public static AdminsListPayload generate(Pageable pageable) {
        AdminsListPayload adminsListPayload = new AdminsListPayload();
        adminsListPayload.setPageable(pageable);
        return adminsListPayload;
    }
}
