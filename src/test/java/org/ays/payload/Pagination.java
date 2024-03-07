package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {

    private int page;
    private int pageSize;

    public static Pagination createPagination() {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setPageSize(10);
        return pagination;
    }

    public static Pagination setPagination(int page, int pageSize) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(pageSize);
        pagination.setPage(page);
        return pagination;
    }

}
