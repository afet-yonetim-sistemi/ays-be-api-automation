package tests.institution.assignmentmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class GetAssignmentTest {
    Assignment assignment=new Assignment();
    String assignmentId;
    Logger logger = LogManager.getLogger(this.getClass());
    @BeforeMethod
    public void setup(){
        assignment= Helper.createANewAssignment();
        assignmentId = Helper.extractUserIdByDescriptionAndName(assignment.getDescription(),assignment.getFirstName(),assignment.getLastName());
    }

    @Test()
    public void getAssignmentPositive() {
        logger.info("IAMS_42 is running");
        Response response = InstitutionEndpoints.getAssignment(assignmentId);
        response.then()
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
    @Test()
    public void getAssignmentAfterDelete() {
        logger.info("IAMS_45 is running");
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
