package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.ApplicationRegistration;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetAdminRegistrationApplicationIdSummaryTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a user I want to get detailed information about administrator registration applications summary when I use valid ID ")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryPositive() {
        String applicationID = ApplicationRegistration.generateApplicationID();
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(true))
                .body("httpStatus", equalTo("OK"))
                .body("response.id", notNullValue())
                .body("response.institution", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a user I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryNegative() {
        String applicationID = "invalidID";
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false))
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("subErrors[0].message", containsString("must be a valid UUID"));

    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a user I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryNegative2() {
        String applicationID = "0d0c71be-7473-4d98-caa8-55dec809c31c";
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false))
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"));

    }
}
