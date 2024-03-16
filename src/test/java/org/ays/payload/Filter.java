package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Filter {

    private List<String> statuses;

    public static Filter generate(ApplicationRegistrationSupportStatus status) {
        Filter filter = new Filter();
        filter.setStatuses(Collections.singletonList(status.toString()));
        return filter;
    }

}
