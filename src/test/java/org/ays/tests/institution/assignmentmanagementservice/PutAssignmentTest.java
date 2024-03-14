package org.ays.tests.institution.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyAssignments;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PutAssignmentTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateAssignmentFirstname() {
        Assignment assignment = InstitutionEndpoints.generateANewAssignment();

        PhoneNumber phoneNumber = new PhoneNumber();
        Response assignmentIdResponse = InstitutionEndpoints.listAssignments(RequestBodyAssignments.generate(phoneNumber));
        String assignmentId = assignmentIdResponse.jsonPath().getString("response.content[0].id");

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
