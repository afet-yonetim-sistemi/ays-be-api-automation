package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersList {

    private List<User> content;
    private int pageNumber;
    private int pageSize;
    private int totalPageCount;
    private int totalElementCount;
    private String sortedBy;
    private String filteredBy;

}
