package tests.user.assignmentmanagementservice;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;
import payload.Location;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;

public class GetAssignmentTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;


    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
        location = new Location();
        assignment = Helper.createANewAssignment();

    }

    @Test
    public void getAssignmentDetailsWhenUserHaveNoAssignment() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .log().body()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("USER DOES NOT HAVE ASSIGNMENT!"))
                .body("isSuccess", equalToObject(false));

    }

    @Test()
    public void getReservedAssignmentDetails() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("USER DOES NOT HAVE ASSIGNMENT!"))
                .body("isSuccess", equalToObject(false));


    }

    @Test()
    public void getAssignedAssignmentDetails() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue())
                .body("response", hasKey("id"))
                .body("response", hasKey("description"))
                .body("response", hasKey("firstName"))
                .body("response", hasKey("lastName"))
                .body("response", hasKey("phoneNumber"))
                .body("response.status", equalTo("ASSIGNED"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }

    @Test()
    public void getInProgressAssignmentDetails() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue())
                .body("response", hasKey("id"))
                .body("response", hasKey("description"))
                .body("response", hasKey("firstName"))
                .body("response", hasKey("lastName"))
                .body("response", hasKey("phoneNumber"))
                .body("response.status", equalTo("IN_PROGRESS"))
                .body("response.location", hasKey("longitude"))
                .body("response.location", hasKey("latitude"));

    }

    @Test()
    public void getDoneAssignmentDetails() {
        location = Helper.generateLocationTR();
        Helper.setSupportStatus("READY", userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        UserEndpoints.completeAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        Response response = UserEndpoints.getAssignmentUser(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(404)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("USER DOES NOT HAVE ASSIGNMENT!"))
                .body("isSuccess", equalToObject(false));

    }

}
