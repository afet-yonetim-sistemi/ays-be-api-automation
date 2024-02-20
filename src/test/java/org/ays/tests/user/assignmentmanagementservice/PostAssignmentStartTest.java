package org.ays.tests.user.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Helper;
import org.ays.payload.Location;
import org.ays.payload.UserCredentials;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostAssignmentStartTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;


    @BeforeClass
    public void setup() {
        userCredentials = Helper.createNewUser();
        location = new Location();
        assignment = Helper.createANewAssignment();

    }

    @Test()
    public void startAssignedAssignment() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test()
    public void startInProgressAssignment() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", allOf(
                        containsString("ASSIGNMENT NOT EXIST!"),
                        containsString("assignmentStatus:ASSIGNED")))
                .body("isSuccess", equalTo(false));
    }
}
