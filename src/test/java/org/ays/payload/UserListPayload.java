package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPageable;
import org.ays.common.model.payload.AysPhoneNumber;

@Getter
@Setter
public class UserListPayload {

    private AysPageable pageable;
    private UsersFilter filter;

    public static UserListPayload generate(AysPhoneNumber phoneNumber) {
        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generateFirstPage());
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        userListPayload.setFilter(filters);
        return userListPayload;
    }

}
