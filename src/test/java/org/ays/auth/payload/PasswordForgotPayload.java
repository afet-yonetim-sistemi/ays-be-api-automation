package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForgotPayload {

    private String emailAddress;

}
