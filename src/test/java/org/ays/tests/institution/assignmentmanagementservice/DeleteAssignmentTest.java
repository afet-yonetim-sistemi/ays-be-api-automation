package org.ays.tests.institution.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Helper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class DeleteAssignmentTest {
    Assignment assignment;
    String assignmentId;

    @BeforeMethod
    public void setup() {
        assignment = Helper.createANewAssignment();
        assignmentId = Helper.extractAssignmentIdByPhoneNumber(assignment.getPhoneNumber());
    }

    @Test()
    public void deleteAssignment() {
        Response response = InstitutionEndpoints.deleteAssignment(assignmentId);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        Assert.assertTrue(InstitutionEndpoints.getAssignment(assignmentId).jsonPath().getString("message").contains("ASSIGNMENT NOT EXIST!"));
    }


    @Test()
    public void deleteAssignmentNegative() {
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
