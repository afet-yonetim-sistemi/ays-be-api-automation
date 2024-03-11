package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRegistration {
    private String institutionId;
    private String reason;

    public static ApplicationRegistration generate(String institutionId, String reason) {
        ApplicationRegistration application = new ApplicationRegistration();
        application.setInstitutionId(institutionId);
        application.setReason(reason);
        return application;
    }
}
