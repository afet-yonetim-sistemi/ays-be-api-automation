package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class FiltersForAssignments {

    private List<String> statuses;
    private PhoneNumber phoneNumber;

    public static FiltersForAssignments generateCreateFilterWithAssignmentPhoneNumber(PhoneNumber phoneNumber) {
        FiltersForAssignments filters = new FiltersForAssignments();
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForAssignments generateCreateFilterWithAssignmentStatus(String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        return filters;
    }

    public static FiltersForAssignments generateCreateFilterWithAssignmentStatusAndLineNumber(String lineNumber, String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setLineNumber(lineNumber);
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForAssignments generateCreateFilterWithAssignmentStatusAndCountryCOde(String countryCode, String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForAssignments generateCreateFilterWithAssignmentStatusPhoneNumber(PhoneNumber phoneNumber, String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

}
