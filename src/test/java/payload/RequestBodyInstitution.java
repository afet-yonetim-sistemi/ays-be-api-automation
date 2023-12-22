package payload;

import java.util.List;

public class RequestBodyInstitution {

    private Pagination pagination;
    private List<Sort> sort;
    private FilterForInstitution filterForInstitution;

    public FilterForInstitution getFilterForInstitution() {
        return filterForInstitution;
    }
    public void setFilterForInstitution(FilterForInstitution filterForInstitution) {
        this.filterForInstitution = filterForInstitution;
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
