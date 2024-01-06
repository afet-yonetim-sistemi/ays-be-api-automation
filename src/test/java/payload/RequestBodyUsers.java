package payload;

import java.util.List;

public class RequestBodyUsers {
    Pagination pagination;
    FiltersForUsers filter;
    private List<Sort> sort;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public FiltersForUsers getFilter() {
        return filter;
    }

    public void setFilter(FiltersForUsers filter) {
        this.filter = filter;
    }

    public List<Sort> getSort() {
        return sort;
    }

    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }
}
