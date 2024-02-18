package org.ays.tests.user.selfmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.User;
import org.ays.payload.UserCredentials;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserSelfTest {
    UserCredentials userCredentials;
    User user = new User();
    String userID;

    @BeforeMethod
    public void setup() {
        user = Helper.createUserPayload();
        userCredentials = Helper.createNewUser(user);
        userID = Helper.extractUserIdByPhoneNumber(user.getPhoneNumber());

    }

    @Test()
    public void getSelfInfoPositive() {
        Response response = UserEndpoints.getUserSelfInfo(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue())
                .body("response.username", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response", Matchers.hasKey("email"))
                .body("response.role", notNullValue())
                .body("response.status", notNullValue())
                .body("response.supportStatus", notNullValue())
                .body("response.phoneNumber", notNullValue())
                .body("response.institution", notNullValue());

    }

    @Test()
    public void getSelfInfoNegative() {
        user.setStatus("PASSIVE");
        user.setRole("VOLUNTEER");
        InstitutionEndpoints.updateUser(userID, user);
        System.out.println(user.getRole());
        System.out.println(user.getStatus());
        Response response = UserEndpoints.getUserSelfInfo(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));

    }

}
