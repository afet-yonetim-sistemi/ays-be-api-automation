package tests.user;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Helper;
import payload.PhoneNumber;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;

public class UserAssignmentManagementServiceTest extends UserEndpoints {

    Faker faker;
    User userPayload;
    PhoneNumber phoneNumber;
    UserCredentials userCredentials;

    @BeforeClass
    public void setup() {
        faker = new Faker();
        userPayload = new User();
        phoneNumber = new PhoneNumber();
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        phoneNumber.setLineNumber(Helper.generateLineNumber());
        phoneNumber.setCountryCode("90");
        userPayload.setPhoneNumber(phoneNumber);
        Response response = InstitutionEndpoints.createAUser(userPayload);
        userCredentials = response.then()
                .statusCode(200)
                .extract().jsonPath().getObject("response", UserCredentials.class);
    }

    @Test(priority = 1)
    public void assignmentSearch() {
        String payload = "{\n" +
                "    \"latitude\": 37.991739,\n" +
                "    \"longitude\": 27.024168\n" +
                "}";
        Response response = UserEndpoints.searchAssignment(payload, userCredentials.getUsername(), userCredentials.getPassword());
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
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 3)
    public void assignmentReject() {
        Response response = UserEndpoints.approveAssignment(userCredentials.getUsername(), userCredentials.getPassword());
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
        Response response = UserEndpoints.updateLocation(payload, userCredentials.getUsername(), userCredentials.getPassword());
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
        Response response = UserEndpoints.startAssignment(userCredentials.getUsername(), userCredentials.getPassword());
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
        Response response = UserEndpoints.updateLocation(payload, userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 7)
    public void assignmentComplete() {
        Response response = UserEndpoints.completeAssignment(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(200)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 8)
    public void assignmentCompleteNegative() {
        Response response = UserEndpoints.completeAssignment(userCredentials.getUsername(), userCredentials.getPassword());
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
