package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private PhoneNumber phoneNumber;
    private String supportStatus;
    private Institution institution;

}
