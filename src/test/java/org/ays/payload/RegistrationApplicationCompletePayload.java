package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;


@Getter
@Setter
public class RegistrationApplicationCompletePayload {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;

    public static RegistrationApplicationCompletePayload generate() {

        RegistrationApplicationCompletePayload completePayload = new RegistrationApplicationCompletePayload();
        completePayload.setUsername(AysRandomUtil.generateUsername());
        completePayload.setEmail(AysRandomUtil.generateEmailAddress());
        completePayload.setPassword(AysRandomUtil.generatePassword());
        completePayload.setFirstName(AysRandomUtil.generateFirstName());
        completePayload.setLastName(AysRandomUtil.generateLastName());
        completePayload.setPhoneNumber(PhoneNumber.generateForTurkey());

        return completePayload;
    }

}
