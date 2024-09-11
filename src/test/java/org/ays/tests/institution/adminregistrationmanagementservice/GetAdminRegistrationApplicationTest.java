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

public class GetAdminRegistrationApplicationTest {
    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to get detailed information about administrator registration applications when I use valid ID")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIDPositive() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        Response response = AdminRegistrationApplicationEndpoints.getRegistrationApplicationId(applicationId);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.createdAt", notNullValue())
                .body("response.id", notNullValue())
                .body("response.createdUser", notNullValue())
                .body("response.status", notNullValue())
                .body("response.institution.id", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationInvalidID() {
        String applicationID = "invalid-id";
        Response response = AdminRegistrationApplicationEndpoints.getRegistrationApplicationId(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be a valid UUID"));
    }
}
