package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class FiltersForUsers {

    private List<String> supportStatuses;
    private List<String> statuses;
    private PhoneNumber phoneNumber;
    private String firstName;
    private String lastName;

    public static FiltersForUsers generateCreateFilterWithUserFirstAndLastName(String firstname, String lastname) {
        FiltersForUsers filters = new FiltersForUsers();
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        return filters;
    }

    public static FiltersForUsers generateCreateFilterWithUserStatus(String status) {
        FiltersForUsers filters = new FiltersForUsers();
        List<String> statuses = Arrays.asList(status);
        filters.setStatuses(statuses);
        return filters;
    }

    public static FiltersForUsers generateCreateFilterWithUserSupportStatus(String supportStatus) {
        FiltersForUsers filters = new FiltersForUsers();
        List<String> statuses = Arrays.asList(supportStatus);
        filters.setSupportStatuses(statuses);
        return filters;
    }

    public static FiltersForUsers generateCreateFilterWithUserPhoneNumber(PhoneNumber phoneNumber) {
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForUsers generateCreateFilterWithAllUserFilters(PhoneNumber phoneNumber, String firstname, String lastname, String status, String supportStatus) {
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        List<String> statuses = Arrays.asList(status);
        filters.setStatuses(statuses);
        List<String> supportStatuses = Arrays.asList(supportStatus);
        filters.setSupportStatuses(supportStatuses);
        return filters;
    }

}
