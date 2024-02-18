package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBodyAssignments {

    private Pagination pagination;
    private FiltersForAssignments filter;

}
