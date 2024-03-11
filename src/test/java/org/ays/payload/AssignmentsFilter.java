package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class AssignmentsFilter {
    private List<String> statuses;
    private PhoneNumber phoneNumber;

    public AssignmentsFilter() {
        this.statuses = new ArrayList<>();
        this.phoneNumber = new PhoneNumber();
    }

    public static AssignmentsFilter generate(String lineNumber, String countryCode, String... statusesToAdd) {
        AssignmentsFilter filters = new AssignmentsFilter();
        Collections.addAll(filters.statuses, statusesToAdd);
        filters.phoneNumber.setLineNumber(lineNumber);
        filters.phoneNumber.setCountryCode(countryCode);
        return filters;
    }

}
