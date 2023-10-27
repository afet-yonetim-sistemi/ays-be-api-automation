package tests.user;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Helper;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserSelfManagementServiceTest extends UserEndpoints {
    UserCredentials userCredentials;
    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeClass
    public void setup() {
        userCredentials = Helper.createNewUser();
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

    @Test(dataProvider = "statusTransitions")
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


}
