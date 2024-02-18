package org.ays.payload;

import java.util.List;

public class UsersList {
    private List<User> content;
    private int pageNumber;
    private int pageSize;
    private int totalPageCount;
    private int totalElementCount;
    private String sortedBy;
    private String filteredBy;

    public List<User> getContent() {
        return content;
    }

    public void setContent(List<User> content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getTotalElementCount() {
        return totalElementCount;
    }

    public void setTotalElementCount(int totalElementCount) {
        this.totalElementCount = totalElementCount;
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }

    public String getFilteredBy() {
        return filteredBy;
    }

    public void setFilteredBy(String filteredBy) {
        this.filteredBy = filteredBy;
    }
}
