package org.ays.payload;

public class RequestBodyAssignments {
    private Pagination pagination;
    private FiltersForAssignments filter;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public FiltersForAssignments getFilter() {
        return filter;
    }

    public void setFilter(FiltersForAssignments filter) {
        this.filter = filter;
    }
}
