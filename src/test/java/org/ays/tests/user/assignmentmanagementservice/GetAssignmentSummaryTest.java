package org.ays.tests.user.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Location;
import org.ays.payload.UserCredentials;
import org.ays.payload.UserSupportStatus;
import org.ays.payload.UserSupportStatusUpdatePayload;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToObject;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;

public class GetAssignmentSummaryTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;


    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = InstitutionEndpoints.generateANewUser();
        location = new Location();
        assignment = InstitutionEndpoints.generateANewAssignment();

    }

    @Test(groups = {"Regression", "User"})
    public void getAssignmentSummaryNegative() {
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalToObject(false));

    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getReservedAssignmentSummary() {
        location = Location.generateForTurkey();

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response", hasKey("id"))
                .body("response.status", equalTo("RESERVED"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getAssignedAssignmentSummary() {
        location = Location.generateForTurkey();

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response", hasKey("id"))
                .body("response.status", equalTo("ASSIGNED"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getInProgressAssignmentSummary() {
        location = Location.generateForTurkey();

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response", hasKey("id"))
                .body("response.status", equalTo("IN_PROGRESS"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }

}
