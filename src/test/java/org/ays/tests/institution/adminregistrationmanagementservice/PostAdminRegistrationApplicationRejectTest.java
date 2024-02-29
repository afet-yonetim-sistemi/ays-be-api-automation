package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.RejectReason;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostAdminRegistrationApplicationRejectTest {
    String applicationID;
    RejectReason reason;

    @BeforeMethod(alwaysRun = true)
    void setup() {
        applicationID = Helper.getAdminRegistrationApplicationID();
        reason = new RejectReason();
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin when I approve an application with WAITING status I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectANotCompletedApplication() {
        reason.setRejectReason("Example reject reason above forty characters.");
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID, reason);
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("isSuccess", equalTo(false))
                .body("message", anyOf(
                        containsString("ADMIN USER REGISTER APPLICATION NOT EXIST!"),
                        containsString("WAITING")));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidRejectReason", dataProviderClass = DataProvider.class)
    @Story("As a super admin when I approve an application with reason field less than 40 or more than 512 characters I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectAnApplicationWithInvalidReason(String invalidRejectReason) {
        reason.setRejectReason(invalidRejectReason);
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID, reason);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("size must be between 40 and 512"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo(invalidRejectReason));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin when I reject an application with invalidID I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectAnApplicationWithInvalidApplicationId() {
        applicationID = "invalidApplicationID";
        reason.setRejectReason("Example reject reason above forty characters.");
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID, reason);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin when I reject an application with null/missing reason field I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectAnApplicationWithMissingReasonField() {
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID, reason);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"));
    }

    private ResponseSpecification badRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("BAD_REQUEST"))
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

}
