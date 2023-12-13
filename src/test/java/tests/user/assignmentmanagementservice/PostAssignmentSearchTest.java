package tests.user.assignmentmanagementservice;

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

public class PostAssignmentSearchTest {
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
    @Test()
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

}
