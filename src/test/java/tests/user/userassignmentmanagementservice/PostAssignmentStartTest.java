package tests.user.userassignmentmanagementservice;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;
import payload.Location;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;

public class PostAssignmentStartTest {
    UserCredentials userCredentials;
    Logger logger = LogManager.getLogger(this.getClass());
    Location location;
    Assignment assignment;


    @BeforeClass
    public void setup() {
        userCredentials = Helper.createNewUser();
        location = new Location();
        assignment=Helper.createANewAssignment();

    }
    @Test()
    public void startAssignedAssignment() {
        logger.info("Test case UMS_09 is running");
        location=Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }
    @Test()
    public void startInProgressAssignment() {
        location=Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", allOf(
                        containsString("ASSIGNMENT NOT EXIST!"),
                        containsString("assignmentStatus:ASSIGNED")))
                .body("isSuccess", equalTo(false));
    }
}
