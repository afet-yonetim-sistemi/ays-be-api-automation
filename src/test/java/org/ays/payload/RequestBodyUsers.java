package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBodyUsers {
    private Pageable pageable;
    private UsersFilter filter;

    public static RequestBodyUsers generate(PhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

    public static RequestBodyUsers generate2(Pageable pageable, UsersFilter usersFilter) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(pageable);
        requestBodyUsers.setFilter(usersFilter);

        return requestBodyUsers;
    }

}
