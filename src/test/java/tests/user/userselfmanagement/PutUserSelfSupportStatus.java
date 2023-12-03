package tests.user.userselfmanagement;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static payload.Helper.createPayloadWithSupportStatus;

public class PutUserSelfSupportStatus {
    UserCredentials userCredentials;
    User user;
    String userID;
    Logger logger = LogManager.getLogger(this.getClass());
    @BeforeMethod
    public void setup() {
        user=Helper.createUserPayload();
        userCredentials = Helper.createNewUser(user);
        userID = Helper.getUserIDByFirstName(user.getFirstName());
        user = Helper.getUser(userID);
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

}
