package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.ApplicationRegistration;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostAdminRegistrationApplicationApproveTest {
    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin when I approve an application with WAITING or REJECTED status I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void approveANotCompletedApplication() {
        String applicationID = ApplicationRegistration.generateApplicationID();
        Response response = InstitutionEndpoints.postRegistrationApplicationApprove(applicationID);
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("isSuccess", equalTo(false))
                .body("message", anyOf(
                        containsString("ADMIN USER REGISTER APPLICATION NOT EXIST!"),
                        containsString("COMPLETED")));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin when I approve an application with invalidID I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void approveApplicationWithInvalidId() {
        Response response = InstitutionEndpoints.postRegistrationApplicationApprove("invalidApplicationID");
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

}
