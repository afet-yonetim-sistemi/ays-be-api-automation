package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestBodyUsers {
    private Pageable pageable;
    private UsersFilter filter;
    private List<Orders> orders;

    public static RequestBodyUsers generate(PhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

}
