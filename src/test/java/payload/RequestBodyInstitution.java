package payload;

import java.util.List;

public class RequestBodyInstitution {

    private Pagination pagination;
    private List<Sort> sort;
    private Filter filter;

    public Filter getFilter() {
        return filter;
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }public Pagination getPagination() {
        return pagination;
    }
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
    public List<Sort> getSort() {
        return sort;
    }public void setSort(List<Sort> sort) {
        this.sort = sort;
    }


}
