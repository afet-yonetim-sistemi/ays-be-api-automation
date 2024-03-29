package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestBodyUsers {
    private Pagination pagination;
    private UsersFilter filter;
    private List<Sort> sort;

    public static RequestBodyUsers generate(PhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPagination(Pagination.generateFirstPage());
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

}
