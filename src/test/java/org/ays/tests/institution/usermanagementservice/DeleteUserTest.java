package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.auth.payload.UserListPayload;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteUserTest {
    String userID;

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void deleteUser() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        UserEndpoints.createAUser(userCreatePayload);

        AysPhoneNumber phoneNumber = userCreatePayload.getPhoneNumber();
        Response response = UserEndpoints.listUsers(UserListPayload.generate(phoneNumber));
        userID = response.jsonPath().getString("response.content[0].id");

        Response deleteResponse = UserEndpoints.deleteUser(userID);
        deleteResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response getResponse = UserEndpoints.getUser(userID);
        getResponse.then()
                .statusCode(200)
                .body("response.status", equalTo("DELETED"));

    }


    @Test(groups = {"Regression", "Institution"})
    public void deleteUserNegative() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        UserEndpoints.createAUser(userCreatePayload);

        AysPhoneNumber phoneNumber = userCreatePayload.getPhoneNumber();
        Response userIDResponse = UserEndpoints.listUsers(UserListPayload.generate(phoneNumber));
        userID = userIDResponse.jsonPath().getString("response.content[0].id");

        UserEndpoints.deleteUser(userID);
        Response response = UserEndpoints.deleteUser(userID);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("USER IS ALREADY DELETED!"));

    }

}
