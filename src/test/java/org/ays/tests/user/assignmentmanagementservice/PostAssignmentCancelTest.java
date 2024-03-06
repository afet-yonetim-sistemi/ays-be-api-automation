package org.ays.tests.user.assignmentmanagementservice;


import io.restassured.response.Response;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Helper;
import org.ays.payload.Location;
import org.ays.payload.Reason;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysRandomUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToObject;
import static org.hamcrest.Matchers.notNullValue;

public class PostAssignmentCancelTest {
    Assignment assignment;
    UserCredentials userCredentials;
    Location location;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = Helper.createNewUser();
        assignment = Helper.createANewAssignment();
        location = Location.generateLocationTR();
    }

    @Test(groups = {"Regression", "User"})
    public void cancelAssignmentNegative() {
        Reason reason = new Reason();
        reason.setReason("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        Response response = UserEndpoints.cancelAssignment(reason, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalToObject(false));
    }

    @Test(groups = {"Regression", "User"})
    public void cancelAssignmentAsReserved() {
        Reason reason = new Reason();
        reason.setReason("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.getAssignmentSummaryUser(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.cancelAssignment(reason, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalToObject(false));

    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void cancelAssignmentPositiveAssigned() {
        Reason reason = new Reason();
        reason.setReason("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Location.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.cancelAssignment(reason, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void cancelAssignmentPositiveInProgress() {
        Reason reason = new Reason();
        reason.setReason("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Location.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.cancelAssignment(reason, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(groups = {"Regression", "User"})
    public void cancelAssignmentWithNullReasonField() {
        Reason reason = new Reason();
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Location.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.cancelAssignment(reason, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("reason"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "User"}, dataProvider = "reasonsProvider")
    public void cancelAssignmentWithInvalidReason(String reasonData) {
        Reason reason = new Reason();
        reason.setReason(reasonData);
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Location.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.cancelAssignment(reason, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @DataProvider(name = "reasonsProvider")
    public Object[][] reasonDataProvider() {
        return new Object[][]{
                {AysRandomUtil.generateString(513)},
                {AysRandomUtil.generateString(20)},
                {""},
                {" ".repeat(40)},
        };
    }


}
