package tests.user;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utility.DatabaseUtility;

import static org.hamcrest.Matchers.*;

public class UserAssignmentManagementServiceTest extends UserEndpoints {
    DatabaseUtility databaseUtility = new DatabaseUtility();

    @BeforeClass()
    public void beforeClass() {
        databaseUtility.connect();
        databaseUtility.updateUserStatus("READY");
        databaseUtility.updateAssignments();
        databaseUtility.disconnect();
    }

    @Test(priority = 1)
    public void assignmentSearch() {
        String payload = "{\n" +
                "    \"latitude\": 37.991739,\n" +
                "    \"longitude\": 27.024168\n" +
                "}";
        Response response = UserEndpoints.searchAssignment(payload);
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.createdUser", notNullValue())
                .body("response.updatedUser", notNullValue())
                .body("response.id", notNullValue())
                .body("response.description", notNullValue())
                .body("response.location.longitude", notNullValue())
                .body("response.location.latitude", notNullValue())
                .body("response.status", equalTo("RESERVED"));
    }

    @Test(priority = 2)
    public void assignmentApprove() {
        Response response = UserEndpoints.approveAssignment();
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 3)
    public void assignmentReject() {
        Response response = UserEndpoints.approveAssignment();
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }

    @Test(priority = 4)
    public void updateLocationBeforeStart() {
        String payload = "{\n" +
                "    \"latitude\": 35.991739,\n" +
                "    \"longitude\": 29.024168\n" +
                "}";
        Response response = UserEndpoints.updateLocation(payload);
        response.then()
                .statusCode(500)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("header", equalTo("PROCESS ERROR"))
                .body("message", containsString("USER LOCATION CANNOT BE UPDATED BECAUSE USER DOES NOT HAVE AN ASSIGNMENT IN PROGRESS! "))
                .body("isSuccess", equalTo(false));
    }

    @Test(priority = 5)
    public void assignmentStart() {
        Response response = UserEndpoints.startAssignment();
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 6)
    public void updateLocationAfterStart() {
        String payload = "{\n" +
                "    \"latitude\": 35.991739,\n" +
                "    \"longitude\": 29.024168\n" +
                "}";
        Response response = UserEndpoints.updateLocation(payload);
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 7)
    public void assignmentComplete() {
        Response response = UserEndpoints.completeAssignment();
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 8)
    public void assignmentCompleteNegative() {
        Response response = UserEndpoints.completeAssignment();
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", allOf(
                        containsString("ASSIGNMENT NOT EXIST!"),
                        containsString("assignmentStatus:IN_PROGRESS")))
                .body("isSuccess", equalTo(false));
    }

}
