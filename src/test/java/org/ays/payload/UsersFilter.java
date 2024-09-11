package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersFilter {
    private String firstName;
    private String lastName;
    private String city;
    private List<String> statuses;
    private AysPhoneNumber phoneNumber;

    public static UsersFilter generate(AysPhoneNumber phoneNumber, String firstname, String lastname, String city, List<String> statuses) {
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        filters.setCity(city);
        filters.setStatuses(statuses);
        return filters;
    }

}
