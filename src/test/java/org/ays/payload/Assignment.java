package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assignment {

    private String description;
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;
    private Double latitude;
    private Double longitude;

}
