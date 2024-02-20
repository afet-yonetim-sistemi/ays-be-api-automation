package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FiltersForUsers {

    private List<String> supportStatuses;
    private List<String> statuses;
    private PhoneNumber phoneNumber;
    private String firstName;
    private String lastName;

}
