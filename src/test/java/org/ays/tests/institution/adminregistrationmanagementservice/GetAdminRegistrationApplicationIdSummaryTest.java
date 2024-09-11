package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class GetAdminRegistrationApplicationIdSummaryTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a user I want to get detailed information about administrator registration applications summary when I use valid ID ")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryPositive() {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        Response response = AdminRegistrationApplicationEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.id", notNullValue())
                .body("response.institution", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a user I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryNegative() {
        String applicationID = "invalidID";
        Response response = AdminRegistrationApplicationEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be a valid UUID"));

    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a user I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryNegative2() {
        String applicationID = "0d0c71be-7473-4d98-caa8-55dec809c31c";
        Response response = AdminRegistrationApplicationEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());

    }
}
