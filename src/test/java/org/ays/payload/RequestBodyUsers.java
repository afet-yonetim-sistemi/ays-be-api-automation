package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPageable;

@Getter
@Setter
public class RequestBodyUsers {
    private AysPageable pageable;
    private UsersFilter filter;

    public static RequestBodyUsers generate(PhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(AysPageable.generateFirstPage());
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

    public static RequestBodyUsers generate2(AysPageable pageable, UsersFilter usersFilter) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(pageable);
        requestBodyUsers.setFilter(usersFilter);

        return requestBodyUsers;
    }

}
