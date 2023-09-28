package tests.user;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserSelfManagementServiceTest extends UserEndpoints {
    String supportStatus;
    String payload;

    @Test(priority = 1)
    public void updateSupportStatusFromIdleToReady() {
        supportStatus = "READY";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue());

    }

    @Test(priority = 2)
    public void updateSupportStatusFromReadyToIdle() {
        supportStatus = "IDLE";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue());
    }

    @Test(priority = 3)
    public void updateSupportStatusFromIdleToBusy() {
        supportStatus = "BUSY";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue());

    }

    @Test(priority = 4)
    public void updateSupportStatusFromBusyToReady() {
        supportStatus = "READY";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue());
    }

    @Test(priority = 5)
    public void updateSupportStatusFromReadyToBusy() {
        supportStatus = "BUSY";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue());
    }

    @Test(priority = 6)
    public void updateSupportStatusFromReadyToOnRoad() {
        supportStatus = "ON_ROAD";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
        response.then()
                .statusCode(400)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("IS NOT ACCEPTED"))
                .body("subErrors[0].field", equalTo("supportStatusAccepted"))
                .body("subErrors[0].value", equalTo("false"));

    }

    @Test(priority = 7)
    public void updateSupportStatusFromBusyToOnRoad() {
        supportStatus = "ON_ROAD";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
        response.then()
                .statusCode(400)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("IS NOT ACCEPTED"))
                .body("subErrors[0].field", equalTo("supportStatusAccepted"))
                .body("subErrors[0].value", equalTo("false"));

    }

    @Test(priority = 8, dependsOnMethods = "updateSupportStatusFromBusyToOnRoad")
    public void updateSupportStatusAfterClass() {
        supportStatus = "READY";
        payload = createPayloadWithSupportStatus(supportStatus);
        Response response = UserEndpoints.updateSupportStatus(payload);
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
