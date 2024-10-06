package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysRandomUtil;


@Getter
@Setter
public class AdminRegistrationApplicationCompletePayload {

    private String username;
    private String emailAddress;
    private String password;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;

    public static AdminRegistrationApplicationCompletePayload generate() {

        AdminRegistrationApplicationCompletePayload completePayload = new AdminRegistrationApplicationCompletePayload();
        completePayload.setUsername(AysRandomUtil.generateUsername());
        completePayload.setEmailAddress(AysRandomUtil.generateEmailAddress());
        completePayload.setPassword(AysRandomUtil.generatePassword());
        completePayload.setFirstName(AysRandomUtil.generateFirstName());
        completePayload.setLastName(AysRandomUtil.generateLastName());
        completePayload.setPhoneNumber(AysPhoneNumber.generateForTurkey());

        return completePayload;
    }

}
