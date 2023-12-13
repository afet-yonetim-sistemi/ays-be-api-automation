package tests.user.selfmanagementservice;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.*;

import static org.hamcrest.Matchers.*;
import static payload.Helper.createPayloadWithSupportStatus;

public class PutUserSelfStatusSupportTest {
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
    @Test(dataProvider = "statusTransitions",priority = 0)
    public void updateSupportStatus(String fromStatus, String toStatus) {
        logger.info("Test case UMS_01 is running");
        String payload = createPayloadWithSupportStatus(toStatus);
        Response response = UserEndpoints.updateSupportStatus(payload, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue());
    }
    @DataProvider(name = "statusTransitions")
    public Object[][] statusTransitions() {
        return new Object[][]{
                {"IDLE", "READY"},
                {"READY", "IDLE"},
                {"IDLE", "BUSY"},
                {"BUSY", "IDLE"},
                {"IDLE", "READY"},
                {"READY", "BUSY"},
                {"BUSY", "READY"}
        };
    }
    @Test()
    public void updateSupportStatusAfterReserveAnAssignment() {
        logger.info("Test case UMS_04 is running");
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
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

}
