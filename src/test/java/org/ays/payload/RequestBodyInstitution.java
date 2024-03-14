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

}
