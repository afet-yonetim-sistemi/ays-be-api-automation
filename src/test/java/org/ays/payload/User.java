package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

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

    public static User generateUserPayload() {

        User userPayload = new User();
        userPayload.setFirstName(AysRandomUtil.generateFirstName());
        userPayload.setLastName(AysRandomUtil.generateLastName());
        userPayload.setPhoneNumber(PhoneNumber.generatePhoneNumber());
        return userPayload;
    }

}
