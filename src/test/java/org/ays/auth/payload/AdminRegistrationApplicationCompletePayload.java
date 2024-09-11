package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.payload.AysPhoneNumber;
import org.ays.utility.AysRandomUtil;


@Getter
@Setter
public class AdminRegistrationApplicationCompletePayload {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;

    public static AdminRegistrationApplicationCompletePayload generate() {

        AdminRegistrationApplicationCompletePayload completePayload = new AdminRegistrationApplicationCompletePayload();
        completePayload.setUsername(AysRandomUtil.generateUsername());
        completePayload.setEmail(AysRandomUtil.generateEmailAddress());
        completePayload.setPassword(AysRandomUtil.generatePassword());
        completePayload.setFirstName(AysRandomUtil.generateFirstName());
        completePayload.setLastName(AysRandomUtil.generateLastName());
        completePayload.setPhoneNumber(AysPhoneNumber.generateForTurkey());

        return completePayload;
    }

}
