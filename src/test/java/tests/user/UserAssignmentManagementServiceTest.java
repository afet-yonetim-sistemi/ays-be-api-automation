package tests.user;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Helper;
import payload.Location;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;

public class UserAssignmentManagementServiceTest extends UserEndpoints {
    UserCredentials userCredentials;
    Logger logger = LogManager.getLogger(this.getClass());
    Location location;

    @BeforeClass
    public void setup() {
        userCredentials = Helper.createNewUser();
        location = new Location();
    }

    @Test(priority = 1)
    public void assignmentSearchNegative() {
        logger.info("Test case UMS_02 is running");
        location = Helper.generateLocation(38, 40, 28, 43);
        Response response = UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());

        response.then()
                .statusCode(409)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("CONFLICT"))
                .body("header", equalTo("ALREADY EXIST"))
                .body("message", containsString("USER NOT READY TO TAKE ASSIGNMENT!"));
    }

    @Test(priority = 2)
    public void assignmentSearchPositive() {
        logger.info("Test case UMS_03 is running");
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        location = Helper.generateLocation(38, 40, 28, 43);
        Response response = UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("isSuccess", equalTo(true))
                .body("response.id", notNullValue())
                .body("response.description", notNullValue())
                .body("response.location.longitude", notNullValue())
                .body("response.location.latitude", notNullValue());

    }

    @Test(priority = 3)
    public void updateSupportStatusAfterReserveAnAssignment() {
        logger.info("Test case UMS_04 is running");
        String status = Helper.createPayloadWithSupportStatus("IDLE");
        Response response = UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(409)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("CONFLICT"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("ALREADY EXIST"))
                .body("message", containsString("USER CANNOT UPDATE SUPPORT STATUS BECAUSE USER HAS ASSIGNMENT!"));

    }

    @Test(priority = 4)
    public void updateLocationWithReservedAssignmentBeforeStart() {
        logger.info("Test case UMS_05 is running");
        location = Helper.generateLocation(38, 40, 28, 43);
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(500)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("header", equalTo("PROCESS ERROR"))
                .body("message", containsString("USER LOCATION CANNOT BE UPDATED BECAUSE USER DOES NOT HAVE AN ASSIGNMENT IN PROGRESS! "))
                .body("isSuccess", equalTo(false));
    }

    @Test(priority = 5)
    public void rejectAssignmentAfterSearch() {
        logger.info("Test case UMS_06 is running");
        Response response = UserEndpoints.rejectAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 6)
    public void assignmentApprove() {
        logger.info("Test case UMS_07 is running");
        location = Helper.generateLocation(38, 40, 28, 43);
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .log().body()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 7)
    public void updateLocationWithAssignedAssignmentBeforeStart() {
        logger.info("Test case UMS_08 is running");
        location = Helper.generateLocation(38, 40, 28, 43);
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(500)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("header", equalTo("PROCESS ERROR"))
                .body("message", containsString("USER LOCATION CANNOT BE UPDATED BECAUSE USER DOES NOT HAVE AN ASSIGNMENT IN PROGRESS! "))
                .body("isSuccess", equalTo(false));
    }

    @Test(priority = 8)
    public void startAssignment() {
        logger.info("Test case UMS_09 is running");
        Response response = UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 9)
    public void updateLocationAfterStart() {
        logger.info("Test case UMS_12 is running");
        location = Helper.generateLocation(38, 40, 28, 43);
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 10)
    public void assignmentComplete() {
        logger.info("Test case UMS_13 is running");
        Response response = UserEndpoints.completeAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 11)
    public void assignmentCompleteNegative() {
        Response response = UserEndpoints.completeAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", allOf(
                        containsString("ASSIGNMENT NOT EXIST!"),
                        containsString("assignmentStatus:IN_PROGRESS")))
                .body("isSuccess", equalTo(false));
    }

}
