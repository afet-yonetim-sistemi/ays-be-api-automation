package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;

import static org.hamcrest.Matchers.equalTo;

public class PutUserTest {
    User user = new User();
    String userID;

    @BeforeMethod
    public void setup() {
        user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        userID = Helper.extractUserIdByPhoneNumber(user.getPhoneNumber());
    }

    @Test()
    public void updateUserAsPassive() {
        user.setStatus("PASSIVE");
        user.setRole("VOLUNTEER");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        Assert.assertEquals(user.getStatus(), "PASSIVE");

    }

    @Test()
    public void updateUserAsActive() {
        user.setRole("VOLUNTEER");
        user.setStatus("PASSIVE");
        InstitutionEndpoints.updateUser(userID, user);
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        Assert.assertEquals(user.getStatus(), "ACTIVE");
    }

    @Test()
    public void updateUserWithInvalidRole() {
        user.setRole("VOL");
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @Test()
    public void updateUserWithBlankRole() {
        user.setRole("");
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @Test()
    public void updateUserWithBlankStatus() {
        user.setRole("VOLUNTEER");
        user.setStatus("");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }
}
