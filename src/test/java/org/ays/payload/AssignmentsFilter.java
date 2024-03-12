package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignmentsFilter {
    private List<String> statuses;
    private PhoneNumber phoneNumber;

    public static AssignmentsFilter generate(PhoneNumber phoneNumber, List<String> statuses) {
        AssignmentsFilter filters = new AssignmentsFilter();
        filters.setStatuses(statuses);
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

}
