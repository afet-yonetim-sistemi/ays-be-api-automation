package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;


@Getter
@Setter
public class RequestBodyForRegistrationIDComplete {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;

    public static RequestBodyForRegistrationIDComplete generateRequestBody() {

        RequestBodyForRegistrationIDComplete registrationIDComplete = new RequestBodyForRegistrationIDComplete();
        registrationIDComplete.setUsername(AysRandomUtil.generateUsername());
        registrationIDComplete.setEmail(AysRandomUtil.generateEmailAddress());
        registrationIDComplete.setPassword(AysRandomUtil.generatePassword());
        registrationIDComplete.setFirstName(AysRandomUtil.generateFirstName());
        registrationIDComplete.setLastName(AysRandomUtil.generateLastName());
        registrationIDComplete.setPhoneNumber(PhoneNumber.generateForTurkey());

        return registrationIDComplete;
    }

}
