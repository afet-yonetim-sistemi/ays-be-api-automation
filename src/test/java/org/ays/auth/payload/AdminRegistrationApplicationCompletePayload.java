package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysRandomUtil;

@Getter
@Setter
public class AdminRegistrationApplicationCompletePayload {

    private String firstName;
    private String lastName;
    private String city;
    private String emailAddress;
    private String password;
    private AysPhoneNumber phoneNumber;

    public static AdminRegistrationApplicationCompletePayload generate() {
        AdminRegistrationApplicationCompletePayload completePayload = new AdminRegistrationApplicationCompletePayload();
        completePayload.setFirstName(AysRandomUtil.generateFirstName());
        completePayload.setLastName(AysRandomUtil.generateLastName());
        completePayload.setCity(AysRandomUtil.generateRandomCity());
        completePayload.setEmailAddress(AysRandomUtil.generateEmailAddress());
        completePayload.setPassword(AysRandomUtil.generatePassword());
        completePayload.setPhoneNumber(AysPhoneNumber.generateForTurkey());
        return completePayload;
    }

}
