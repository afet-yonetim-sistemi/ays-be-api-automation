package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
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
    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void getRegistrationApplicationIDPositive() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        Response response = AdminRegistrationApplicationEndpoints.findById(id);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.createdAt", notNullValue())
                .body("response.id", notNullValue())
                .body("response.createdUser", notNullValue())
                .body("response.status", notNullValue())
                .body("response.institution.id", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void getRegistrationApplicationInvalidID() {
        String applicationID = "invalid-id";
        Response response = AdminRegistrationApplicationEndpoints.findById(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be a valid UUID"));
    }

}
