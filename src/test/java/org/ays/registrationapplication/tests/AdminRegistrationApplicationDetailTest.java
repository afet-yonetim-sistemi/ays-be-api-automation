package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.datasource.AdminRegistrationApplicationDataSource;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class AdminRegistrationApplicationDetailTest {
    @Test(groups = {"Smoke", "Regression"})
    public void detailRegistrationApplicationPositiveTest() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        Response response = AdminRegistrationApplicationEndpoints.findById(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.createdAt", notNullValue())
                .body("response.id", notNullValue())
                .body("response.createdUser", notNullValue())
                .body("response.status", notNullValue())
                .body("response.institution.id", notNullValue());
    }

    @Test(groups = {"Regression"})
    public void detailRegistrationApplicationInvalidID() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String applicationID = "invalid-id";
        Response response = AdminRegistrationApplicationEndpoints.findById(applicationID, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be a valid UUID"));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
