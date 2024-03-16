package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestBodyInstitution {
    private Pagination pagination;
    private List<Sort> sort;
    private Filter filter;

    public static RequestBodyInstitution generateFilter(Pagination pagination, Filter filter) {
        RequestBodyInstitution requestBodyInstitution = new RequestBodyInstitution();
        requestBodyInstitution.setPagination(pagination);
        requestBodyInstitution.setFilter(filter);

        return requestBodyInstitution;
    }

}
