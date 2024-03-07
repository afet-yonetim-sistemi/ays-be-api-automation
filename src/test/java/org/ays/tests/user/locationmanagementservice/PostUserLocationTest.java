package org.ays.tests.user.locationmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Helper;
import org.ays.payload.Location;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysRandomUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostUserLocationTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;


    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = UserCredentials.generateCreate();
        location = new Location();
        assignment = Helper.createANewAssignment();

    }

    @Test(groups = {"Regression", "User"})
    public void updateLocationWithReservedAssignment() {
        location = Location.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(500)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("header", equalTo("PROCESS ERROR"))
                .body("message", containsString("USER LOCATION CANNOT BE UPDATED BECAUSE USER DOES NOT HAVE AN ASSIGNMENT IN PROGRESS! "))
                .body("isSuccess", equalTo(false));
    }

    @Test(groups = {"Regression", "User"})
    public void updateLocationWithAssignedAssignment() {
        location = Location.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(500)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("header", equalTo("PROCESS ERROR"))
                .body("message", containsString("USER LOCATION CANNOT BE UPDATED BECAUSE USER DOES NOT HAVE AN ASSIGNMENT IN PROGRESS! "))
                .body("isSuccess", equalTo(false));
    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void updateLocationAfterStart() {
        location = Location.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(groups = {"Regression", "User"}, dataProvider = "invalidLongitudeValues")
    public void updateLocationWithInValidLongitude(Double invalidLongitude, String errorMessage) {
        location = Location.generate(75.0, invalidLongitude);
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("longitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo(String.valueOf(invalidLongitude)));
    }

    @DataProvider(name = "invalidLongitudeValues")
    public Object[][] invalidLongitudeValues() {
        return new Object[][]{
                {180.000000001, "must be less than or equal to 180"},
                {-180.000000001, "must be greater than or equal to -180"},
                {-200.0, "must be greater than or equal to -180"},
                {-270.0, "must be greater than or equal to -180"},
                {200.0, "must be less than or equal to 180"},
                {270.0, "must be less than or equal to 180"},
                {180.000000001234, "must be less than or equal to 180"},
                {-180.000000001234, "must be greater than or equal to -180"},
        };
    }

    @Test(groups = {"Regression", "User"}, dataProvider = "invalidLatitudeValues")
    public void updateLocationWithInValidLatitude(Double invalidLatitude, String errorMessage) {
        location = Location.generate(invalidLatitude, 95.0);
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.updateLocation(location, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("latitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo(String.valueOf(invalidLatitude)));
    }

    @DataProvider(name = "invalidLatitudeValues")
    public Object[][] invalidLatitudeValues() {
        return new Object[][]{
                {100.0, "must be less than or equal to 90"},
                {-100.0, "must be greater than or equal to -90"},
                {90.000000001, "must be less than or equal to 90"},
                {-90.000000001, "must be greater than or equal to -90"},
                {-200.0, "must be greater than or equal to -90"},
                {200.0, "must be less than or equal to 90"},

        };
    }


}
