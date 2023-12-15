package tests.user.assignmentmanagementservice;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;
import payload.Location;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalToObject;

public class GetAssignmentSummaryTest {
    UserCredentials userCredentials;
    Logger logger = LogManager.getLogger(this.getClass());
    Location location;
    Assignment assignment;


    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
        location = new Location();
        assignment=Helper.createANewAssignment();

    }
    @Test()
    public void getAssignmentSummaryNegative() {
        logger.info("Test case UMS_22 is running");
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalToObject(false));

    }
    @Test()
    public void getReservedAssignmentSummary() {
        logger.info("Test case UMS_19 is running");
        location=Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response", hasKey("id"))
                .body("response.status", equalTo("RESERVED"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }

    @Test()
    public void getAssignedAssignmentSummary() {
        logger.info("Test case UMS_20 is running");
        location=Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response", hasKey("id"))
                .body("response.status", equalTo("ASSIGNED"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }
    @Test(priority = 15)
    public void getInProgressAssignmentSummary() {
        logger.info("Test case UMS_21 is running");
        location=Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response", hasKey("id"))
                .body("response.status", equalTo("IN_PROGRESS"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }

}