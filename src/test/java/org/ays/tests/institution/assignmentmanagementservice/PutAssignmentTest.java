package org.ays.tests.institution.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Helper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PutAssignmentTest {
    Assignment assignment;
    String assignmentId;

    @BeforeMethod
    public void setup() {
        assignment = Helper.createANewAssignment();
        assignmentId = Helper.extractAssignmentIdByPhoneNumber(assignment.getPhoneNumber());
    }

    @Test()
    public void updateAssignmentFirstname() {
        String expectedName = "updated firstname";
        assignment.setFirstName(expectedName);
        Response response = InstitutionEndpoints.updateAssignment(assignmentId, assignment);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        String actualName = InstitutionEndpoints.getAssignment(assignmentId).jsonPath().getString("response.firstName");
        Assert.assertEquals(expectedName, actualName);

    }
}
