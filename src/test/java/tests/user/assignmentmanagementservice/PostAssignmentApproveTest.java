package tests.user.assignmentmanagementservice;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;
import payload.Location;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;

public class PostAssignmentApproveTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;


    @BeforeClass
    public void setup() {
        userCredentials = Helper.createNewUser();
        location = new Location();
        assignment = Helper.createANewAssignment();

    }

    @Test()
    public void assignmentApproveWhenUserHasNoReservedAssignment() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }

    @Test()
    public void assignmentApprove() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test()
    public void assignmentApproveWhenUserHasAssignedAssignment() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }
}
