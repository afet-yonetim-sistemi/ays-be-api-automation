package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.User;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteUserTest {
    String userID;

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void deleteUser() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response response = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber));
        userID =  response.jsonPath().getString("response.content[0].id");

        Response deleteResponse = InstitutionEndpoints.deleteUser(userID);
        deleteResponse.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));

        Response getResponse = InstitutionEndpoints.getUser(userID);
        getResponse.then()
                .statusCode(200)
                .body("response.status", equalTo("DELETED"));

    }


    @Test(groups = {"Regression", "Institution"})
    public void deleteUserNegative() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber));
        userID =  userIDResponse.jsonPath().getString("response.content[0].id");

        InstitutionEndpoints.deleteUser(userID);
        Response response = InstitutionEndpoints.deleteUser(userID);
        response.then()
                .statusCode(409)
                .contentType("application/json")
                .body("httpStatus", equalTo("CONFLICT"))
                .body("isSuccess", equalTo(false))
                .body("header", containsString("ALREADY EXIST"))
                .body("message", containsString("USER IS ALREADY DELETED!"));

    }

}
