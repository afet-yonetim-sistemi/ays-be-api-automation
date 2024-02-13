package tests.user.selfmanagementservice;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;
import payload.Location;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;
import static payload.Helper.createPayloadWithSupportStatus;
import static payload.Helper.generateLocationTR;

public class PutUserSelfStatusSupportTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;

    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
        location = generateLocationTR();
        assignment = Helper.createANewAssignment();
    }

    @Test(dataProvider = "statusTransitions")
    public void updateSupportStatus(String fromStatus, String toStatus) {
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
