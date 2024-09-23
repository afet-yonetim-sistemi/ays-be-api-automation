package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class AdminRegistrationApplicationCreateTest {
    AdminRegistrationApplicationCreatePayload application;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        application = new AdminRegistrationApplicationCreatePayload();
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplication() {
        application = AdminRegistrationApplicationCreatePayload.generate(AysConfigurationProperty.AfetYonetimSistemi.ID, AysRandomUtil.generateReasonString());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response", hasKey("id"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidDataForPostApplicationReasonField", dataProviderClass = AysDataProvider.class, enabled = false)
    public void createAnAdminRegistrationApplicationWithInvalidInputs(String reason, String message, String field, String type) {
        application = AdminRegistrationApplicationCreatePayload.generate(AysConfigurationProperty.AfetYonetimSistemi.ID, reason);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", anyOf(containsString(message), containsString("must not be blank")))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplicationWithInvalidInstitutionId() {
        application = AdminRegistrationApplicationCreatePayload.generate("invalidId", AysRandomUtil.generateReasonString());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("INSTITUTION NOT EXIST!"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplicationWithMissingInstitutionId() {
        application = AdminRegistrationApplicationCreatePayload.generate(null, AysRandomUtil.generateReasonString());
        application.setInstitutionId(null);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("institutionId"))
                .body("subErrors[0].type", equalTo("String"));
    }


}
