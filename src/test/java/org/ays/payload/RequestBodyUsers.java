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

}
