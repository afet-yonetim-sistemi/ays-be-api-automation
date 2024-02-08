package tests.institution.assignmentmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;

import static org.hamcrest.Matchers.equalTo;

public class PutAssignmentTest {
    Assignment assignment = new Assignment();
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
