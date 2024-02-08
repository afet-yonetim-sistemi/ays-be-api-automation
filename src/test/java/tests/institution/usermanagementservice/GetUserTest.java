package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;

import static org.hamcrest.Matchers.notNullValue;

public class GetUserTest {
    String userID;

    @BeforeMethod
    public void setup() {
        User user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        userID = Helper.extractUserIdByPhoneNumber(user.getPhoneNumber());
    }

    @Test()
    public void getUser() {
        Response response = InstitutionEndpoints.getUser(userID);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("response.createdUser", notNullValue())
                .body("response.id", notNullValue())
                .body("response.username", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response.role", notNullValue())
                .body("response.status", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.supportStatus", notNullValue());
    }
}
