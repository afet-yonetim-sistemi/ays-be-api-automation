package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.User;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetUserTest {
    String userID;

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void getUser() {
        User user = User.generate();
        UserEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = UserEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber), LoginPayload.generateAsAdminUserOne());
        userID = userIDResponse.jsonPath().getString("response.content[0].id");

        Response response = UserEndpoints.getUser(userID);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
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
