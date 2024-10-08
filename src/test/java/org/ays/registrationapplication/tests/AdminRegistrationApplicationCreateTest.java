package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class AdminRegistrationApplicationCreateTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplication() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationCreatePayload application = AdminRegistrationApplicationCreatePayload
                .generate(AysConfigurationProperty.TestVolunteerFoundation.ID, AysRandomUtil.generateReasonString());

        Response response = AdminRegistrationApplicationEndpoints.create(application, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response", hasKey("id"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidDataForPostApplicationReasonField", dataProviderClass = AysDataProvider.class, enabled = false)
    public void createAnAdminRegistrationApplicationWithInvalidInputs(String reason, String message, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationCreatePayload application = AdminRegistrationApplicationCreatePayload
                .generate(AysConfigurationProperty.TestVolunteerFoundation.ID, reason);

        Response response = AdminRegistrationApplicationEndpoints.create(application, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", anyOf(containsString(message), containsString("must not be blank")))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplicationWithInvalidInstitutionId() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationCreatePayload application = AdminRegistrationApplicationCreatePayload
                .generate("invalidId", AysRandomUtil.generateReasonString());

        Response response = AdminRegistrationApplicationEndpoints.create(application, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("institution does not exist! ID:invalidId"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplicationWithMissingInstitutionId() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationCreatePayload application = AdminRegistrationApplicationCreatePayload
                .generate(null, AysRandomUtil.generateReasonString());

        application.setInstitutionId(null);
        Response response = AdminRegistrationApplicationEndpoints.create(application, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("institutionId"))
                .body("subErrors[0].type", equalTo("String"));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
