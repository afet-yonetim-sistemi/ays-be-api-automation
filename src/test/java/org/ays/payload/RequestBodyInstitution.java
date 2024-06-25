package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestBodyInstitution {
    private Pageable pageable;
    private List<Orders> orders;
    private Filter filter;

    public static RequestBodyInstitution generateFilter(Pageable pageable, Filter filter) {
        RequestBodyInstitution requestBodyInstitution = new RequestBodyInstitution();
        requestBodyInstitution.setPageable(pageable);
        requestBodyInstitution.setFilter(filter);

        return requestBodyInstitution;
    }

}
