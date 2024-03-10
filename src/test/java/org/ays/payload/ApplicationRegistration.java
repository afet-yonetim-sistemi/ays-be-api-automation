package org.ays.payload;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class ApplicationRegistration {

    private String institutionId;
    private String reason;

    public static String generateApplicationID() {
        ApplicationRegistration applicationRegistration = new ApplicationRegistration();
        applicationRegistration.setReason("A valid test string must have forty character.");
        applicationRegistration.setInstitutionId(AysConfigurationProperty.InstitutionOne.ID);
        while (true) {
            Response response = InstitutionEndpoints.postRegistrationAdminApplication(applicationRegistration);
            String applicationID = response.jsonPath().getString("response.id");
            if (applicationID != null) {
                return applicationID;
            }
        }
    }

    public static ApplicationRegistration generate() {
        ApplicationRegistration application = new ApplicationRegistration();
        application.setInstitutionId(AysConfigurationProperty.InstitutionOne.ID);
        application.setReason(AysRandomUtil.generateReasonString());
        return application;
    }

    public static ApplicationRegistration generateApplicationRegistrationPayloadWithoutReason() {
        ApplicationRegistration application = new ApplicationRegistration();
        application.setInstitutionId(AysConfigurationProperty.InstitutionOne.ID);
        return application;
    }

}
