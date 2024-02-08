package tests.user.assignmentmanagementservice;


import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.*;

import static org.hamcrest.Matchers.*;

public class PostAssignmentCancelTest {
    Assignment assignment;
    UserCredentials userCredentials;
    Reason reason;
    Location location;

    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
        assignment = Helper.createANewAssignment();
        location = Helper.generateLocationTR();
    }

    @Test
    public void cancelAssignmentNegative() {
        reason = new Reason();
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

    @Test
    public void cancelAssignmentAsReserved() {
        reason = new Reason();
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

    @Test
    public void cancelAssignmentPositiveAssigned() {
        reason = new Reason();
        reason.setReason("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Helper.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.cancelAssignment(reason, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test
    public void cancelAssignmentPositiveInProgress() {
        reason = new Reason();
        reason.setReason("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Helper.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
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

    @Test
    public void cancelAssignmentWithNullReasonField() {
        reason = new Reason();
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Helper.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
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

    @Test(dataProvider = "reasonsProvider")
    public void cancelAssignmentWithInvalidReason(String reasonData) {
        reason = new Reason();
        reason.setReason(reasonData);
        String status = Helper.createPayloadWithSupportStatus("READY");
        UserEndpoints.updateSupportStatus(status, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(Helper.generateLocationTR(), userCredentials.getUsername(), userCredentials.getPassword());
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
                {Helper.generateString(513)},
                {Helper.generateString(20)},
                {""},
                {" ".repeat(40)},
        };
    }


}
