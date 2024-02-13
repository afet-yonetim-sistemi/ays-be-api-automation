package tests.institution.assignmentmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;

import static org.hamcrest.Matchers.*;

public class GetAssignmentTest {
    Assignment assignment = new Assignment();
    String assignmentId;

    @BeforeMethod
    public void setup() {
        assignment = Helper.createANewAssignment();
        assignmentId = Helper.extractAssignmentIdByPhoneNumber(assignment.getPhoneNumber());
    }

    @Test()
    public void getAssignmentPositive() {
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

    @Test()
    public void getAssignmentAfterDelete() {
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
