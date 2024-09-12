package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPageable;
import org.ays.common.model.payload.AysPhoneNumber;

@Getter
@Setter
public class RequestBodyUsers {
    private AysPageable pageable;
    private UsersFilter filter;

    public static RequestBodyUsers generate(AysPhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(AysPageable.generateFirstPage());
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

}
