package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Sort {

    private String property;
    private String direction;

    public static List<Sort> generateCreateSortBody(String property, String direction) {
        Sort sort = new Sort();
        sort.setDirection(direction);
        sort.setProperty(property);
        List<Sort> sortList = Arrays.asList(sort);
        return sortList;
    }

}
