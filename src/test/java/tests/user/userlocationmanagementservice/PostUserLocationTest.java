package tests.user.userlocationmanagementservice;

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
import static org.hamcrest.Matchers.equalTo;

public class PostUserLocationTest {
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
    public void updateLocationWithReservedAssignment() {
        logger.info("Test case UMS_05 is running");
        location = Helper.generateLocation(38, 40, 28, 43);
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(500)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("header", equalTo("PROCESS ERROR"))
                .body("message", containsString("USER LOCATION CANNOT BE UPDATED BECAUSE USER DOES NOT HAVE AN ASSIGNMENT IN PROGRESS! "))
                .body("isSuccess", equalTo(false));
    }
    @Test()
    public void updateLocationWithAssignedAssignment() {
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
    @Test()
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


}
