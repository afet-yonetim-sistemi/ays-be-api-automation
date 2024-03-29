package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBodyAssignments {

    private Pagination pagination;
    private AssignmentsFilter filter;

    public static RequestBodyAssignments generateRequestBodyAssignments(int page, int pageSize) {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        requestBodyAssignments.setPagination(pagination);
        return requestBodyAssignments;
    }

    public static RequestBodyAssignments generate(PhoneNumber phoneNumber) {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        requestBodyAssignments.setPagination(Pagination.generateFirstPage());
        AssignmentsFilter filters = new AssignmentsFilter();
        filters.setPhoneNumber(phoneNumber);
        requestBodyAssignments.setFilter(filters);
        return requestBodyAssignments;
    }

}
