package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Filter {

    private List<String> statuses;

    public static Filter generate(AdminRegistrationApplicationStatus status) {
        Filter filter = new Filter();
        filter.setStatuses(Collections.singletonList(status.toString()));
        return filter;
    }

}
