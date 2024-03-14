package org.ays.tests.user.selfmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Location;
import org.ays.payload.UserCredentials;
import org.ays.payload.UserSupportStatus;
import org.ays.payload.UserSupportStatusUpdatePayload;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PutUserSelfStatusSupportTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = UserCredentials.generateCreate();
        location = Location.generateLocationTR();
        assignment = Assignment.generateCreateAssignment();
    }

    @Test(groups = {"Smoke", "Regression", "User"}, dataProvider = "statusTransitions")
    public void updateSupportStatus(String fromStatus, String toStatus) {

        UserSupportStatus userSupportStatus = UserSupportStatus.valueOf(toStatus);
        Response response = UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(userSupportStatus),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
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

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());

        Response response = UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.IDLE),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        response.then()
                .statusCode(409)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("CONFLICT"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("ALREADY EXIST"))
                .body("message", containsString("USER CANNOT UPDATE SUPPORT STATUS BECAUSE USER HAS ASSIGNMENT!"));

    }

}
