package org.ays.tests.user.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Location;
import org.ays.payload.UserCredentials;
import org.ays.payload.UserSupportStatus;
import org.ays.payload.UserSupportStatusUpdatePayload;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostAssignmentApproveTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;


    @BeforeClass(alwaysRun = true)
    public void setup() {
        userCredentials = InstitutionEndpoints.generateANewUser();
        location = new Location();
        assignment = InstitutionEndpoints.generateANewAssignment();

    }

    @Test(groups = {"Regression", "User"})
    public void assignmentApproveWhenUserHasNoReservedAssignment() {
        location = Location.generateForTurkey();

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void assignmentApprove() {
        location = Location.generateForTurkey();

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(groups = {"Regression", "User"})
    public void assignmentApproveWhenUserHasAssignedAssignment() {
        location = Location.generateForTurkey();

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }
}
