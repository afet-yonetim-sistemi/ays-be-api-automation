package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestBodyUsers {

    private Pagination pagination;
    private FiltersForUsers filter;
    private List<Sort> sort;

    public static RequestBodyUsers generateCreateRequestBodyUsersWithPhoneNumberFilter(PhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPagination(Pagination.createPagination());
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

}
