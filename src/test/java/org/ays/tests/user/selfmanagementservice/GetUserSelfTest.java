package org.ays.tests.user.selfmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.*;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserSelfTest {
    UserCredentials userCredentials;
    String userID;

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getSelfInfoPositive() {
        User user = User.generate();
        userCredentials = InstitutionEndpoints.generateANewUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber));
        userID =  userIDResponse.jsonPath().getString("response.content[0].id");


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

    @Test(groups = {"Regression", "User"})
    public void getSelfInfoNegative() {
        User user = User.generate();
        userCredentials = InstitutionEndpoints.generateANewUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber));
        userID =  userIDResponse.jsonPath().getString("response.content[0].id");

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
