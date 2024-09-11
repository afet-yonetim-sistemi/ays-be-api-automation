package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationApplicationCreatePayload {
    private String institutionId;
    private String reason;

    public static AdminRegistrationApplicationCreatePayload generate(String institutionId, String reason) {
        AdminRegistrationApplicationCreatePayload application = new AdminRegistrationApplicationCreatePayload();
        application.setInstitutionId(institutionId);
        application.setReason(reason);
        return application;
    }
}
