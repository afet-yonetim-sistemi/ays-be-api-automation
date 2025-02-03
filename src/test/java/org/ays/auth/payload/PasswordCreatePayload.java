package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.AysRandomUtil;

@Getter
@Setter
public class PasswordCreatePayload {

    private String password;
    private String passwordRepeat;

    public static PasswordCreatePayload generate() {
        PasswordCreatePayload passwordCreatePayload = new PasswordCreatePayload();
        String generatedPassword = AysRandomUtil.generatePassword();
        passwordCreatePayload.setPassword(generatedPassword);
        passwordCreatePayload.setPasswordRepeat(generatedPassword);
        return passwordCreatePayload;
    }

}
