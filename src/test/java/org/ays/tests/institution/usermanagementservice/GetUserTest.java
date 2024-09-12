package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.payload.RequestBodyUsers;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetUserTest {
    String userID;

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void getUser() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        UserEndpoints.createAUser(userCreatePayload);

        AysPhoneNumber phoneNumber = userCreatePayload.getPhoneNumber();
        Response userIDResponse = UserEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber));
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
