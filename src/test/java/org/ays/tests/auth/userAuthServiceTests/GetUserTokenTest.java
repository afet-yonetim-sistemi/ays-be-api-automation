package org.ays.tests.auth.userAuthServiceTests;

import io.restassured.response.Response;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetUserTokenTest {
    UserCredentials userCredentials;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = UserCredentials.generateCreate();
    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getTokenForValidUser() {
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithInvalidPassword() {
        userCredentials.setPassword("wrongPassword");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithInvalidUsername() {
        userCredentials.setUsername("wrongUserName");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());

    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithNullUsername() {
        userCredentials.setUsername(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectNullCredentialFieldErrorSpec("username"));
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithNullPassword() {
        userCredentials.setPassword(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectNullCredentialFieldErrorSpec("password"));
    }
}
