package org.ays.tests.institution.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyAssignments;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class DeleteAssignmentTest {
    Assignment assignment;
    String assignmentId;

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void deleteAssignment() {
        assignment = InstitutionEndpoints.generateANewAssignment();

        PhoneNumber phoneNumber = new PhoneNumber();
        Response assignmentIdResponse = InstitutionEndpoints.listAssignments(RequestBodyAssignments.generate(phoneNumber));
        assignmentId = assignmentIdResponse.jsonPath().getString("response.content[0].id");

        Response response = InstitutionEndpoints.deleteAssignment(assignmentId);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        Assert.assertTrue(InstitutionEndpoints.getAssignment(assignmentId).jsonPath().getString("message").contains("ASSIGNMENT NOT EXIST!"));
    }


    @Test(groups = {"Regression", "Institution"})
    public void deleteAssignmentNegative() {
        assignment = InstitutionEndpoints.generateANewAssignment();

        PhoneNumber phoneNumber = new PhoneNumber();
        Response assignmentIdResponse = InstitutionEndpoints.listAssignments(RequestBodyAssignments.generate(phoneNumber));
        assignmentId = assignmentIdResponse.jsonPath().getString("response.content[0].id");

        InstitutionEndpoints.deleteAssignment(assignmentId);
        Response response = InstitutionEndpoints.deleteAssignment(assignmentId);
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
