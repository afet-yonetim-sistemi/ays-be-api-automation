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

public class AdminRegistrationApplicationSummaryTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void getRegistrationApplicationIdSummaryPositive() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        Response response = AdminRegistrationApplicationEndpoints.findSummaryById(id);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.id", notNullValue())
                .body("response.institution", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void getRegistrationApplicationIdSummaryNegative() {
        String applicationID = "invalidID";
        Response response = AdminRegistrationApplicationEndpoints.findSummaryById(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be a valid UUID"));

    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void getRegistrationApplicationIdSummaryNegative2() {
        String applicationID = "0d0c71be-7473-4d98-caa8-55dec809c31c";
        Response response = AdminRegistrationApplicationEndpoints.findSummaryById(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());

    }
}
