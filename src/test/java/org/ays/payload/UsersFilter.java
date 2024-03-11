package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class UsersFilter {
    private String firstName;
    private String lastName;
    private List<String> supportStatuses;
    private List<String> statuses;
    private PhoneNumber phoneNumber;


    public UsersFilter() {
        this.supportStatuses = new ArrayList<>();
        this.statuses = new ArrayList<>();
        this.phoneNumber = new PhoneNumber();
    }

    public static UsersFilter generate(PhoneNumber phoneNumber, String firstname, String lastname, String status, String... supportStatusToAdd) {
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        if (supportStatusToAdd != null) {
            filters.setSupportStatuses(Arrays.asList(supportStatusToAdd));
        } else {
            filters.setSupportStatuses(new ArrayList<>());
        }

        if (status != null) {
            filters.setStatuses(Arrays.asList(status));
        } else {
            filters.setStatuses(new ArrayList<>());
        }
        return filters;
    }

}
