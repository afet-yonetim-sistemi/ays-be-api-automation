package tests.institution.assignmentmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;

import static org.hamcrest.Matchers.equalTo;

public class PutAssignmentTest {
    Assignment assignment=new Assignment();
    String assignmentId;
    Logger logger = LogManager.getLogger(this.getClass());
    @BeforeMethod
    public void setup(){
        assignment= Helper.createANewAssignment();
        assignmentId = Helper.extractUserIdByDescriptionAndName(assignment.getDescription(),assignment.getFirstName(),assignment.getLastName());
    }
    @Test()
    public void updateAssignmentFirstname() {
        logger.info("IAMS_43 is running");
        String expectedName= "updated firstname";
        assignment.setFirstName(expectedName);
        Response response = InstitutionEndpoints.updateAssignment(assignmentId, assignment);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        String actualName=InstitutionEndpoints.getAssignment(assignmentId).jsonPath().getString("response.firstName");
        Assert.assertEquals(expectedName,actualName);

    }
}
