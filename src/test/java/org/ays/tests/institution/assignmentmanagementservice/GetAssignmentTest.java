package org.ays.tests.institution.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyAssignments;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class GetAssignmentTest {
    Assignment assignment = new Assignment();
    String assignmentId;

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void getAssignmentPositive() {
        assignment = InstitutionEndpoints.generateANewAssignment();

        PhoneNumber phoneNumber = new PhoneNumber();
        Response assignmentIdResponse = InstitutionEndpoints.listAssignments(RequestBodyAssignments.generate(phoneNumber));
        assignmentId = assignmentIdResponse.jsonPath().getString("response.content[0].id");

        Response response = InstitutionEndpoints.getAssignment(assignmentId);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("response.createdAt", notNullValue())
                .body("response.id", notNullValue())
                .body("response.description", notNullValue())
                .body("response.status", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.location.longitude", notNullValue())
                .body("response.location.latitude", notNullValue())
                .body("response.user", anyOf(nullValue(), notNullValue()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void getAssignmentAfterDelete() {
        assignment = InstitutionEndpoints.generateANewAssignment();

        PhoneNumber phoneNumber = new PhoneNumber();
        Response assignmentIdResponse = InstitutionEndpoints.listAssignments(RequestBodyAssignments.generate(phoneNumber));
        assignmentId = assignmentIdResponse.jsonPath().getString("response.content[0].id");

        InstitutionEndpoints.deleteAssignment(assignmentId);
        Response response = InstitutionEndpoints.getAssignment(assignmentId);
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }
}
