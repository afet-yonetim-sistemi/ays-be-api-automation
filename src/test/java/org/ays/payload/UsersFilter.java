package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersFilter {
    private String firstName;
    private String lastName;
    private List<String> supportStatuses;
    private List<String> statuses;
    private PhoneNumber phoneNumber;

    public static UsersFilter generate(PhoneNumber phoneNumber, String firstname, String lastname, List<String> statuses, List<String> supportStatuses) {
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        filters.setSupportStatuses(supportStatuses);
        filters.setStatuses(statuses);
        return filters;
    }

}
