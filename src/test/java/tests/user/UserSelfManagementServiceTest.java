package tests.user;

import endpoints.InstitutionEndpoints;
import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserSelfManagementServiceTest {
    UserCredentials userCredentials;
    User user;
    String userID;
    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeClass
    public void setup() {
        userCredentials = Helper.createNewUser();
        userID = Helper.getUserID(userCredentials.getUsername());
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

    private String createPayloadWithSupportStatus(String supportStatus) {
        return "{\n" +
                "    \"supportStatus\": \"" + supportStatus + "\"\n" +
                "}";
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
    @Test(priority = 1)
    public void getSelfInfoPositive(){
        logger.info("Test case UMS_14 is running");
        Response response=UserEndpoints.getUserSelfInfo(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue())
                .body("response.username", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response", Matchers.hasKey("email"))
                .body("response.role", notNullValue())
                .body("response.status", notNullValue())
                .body("response.supportStatus", notNullValue())
                .body("response.phoneNumber", notNullValue())
                .body("response.institution", notNullValue());

    }
    @Test(priority = 2)
    public void getSelfInfoNegative(){
        logger.info("Test case UMS_15 is running");
        user.setStatus("PASSIVE");
        user.setRole("VOLUNTEER");
        InstitutionEndpoints.updateUser(userID,user);
        Response response=UserEndpoints.getUserSelfInfo(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));

        user.setStatus("ACTIVE");

    }

}
