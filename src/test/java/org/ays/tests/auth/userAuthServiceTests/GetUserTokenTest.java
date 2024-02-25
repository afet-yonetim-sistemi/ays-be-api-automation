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

    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
    }

    @Test
    public void getTokenForValidUser() {
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec())
                .spec(AysResponseSpecs.getTokenResponseSpec());
    }

    @Test
    public void getUserTokenWithInvalidPassword() {
        userCredentials.setPassword("wrongPassword");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());
    }

    @Test
    public void getUserTokenWithInvalidUsername() {
        userCredentials.setUsername("wrongUserName");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());

    }

    @Test
    public void getUserTokenWithNullUsername() {
        userCredentials.setUsername(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.badRequestResponseSpec())
                .spec(AysResponseSpecs.nullCredentialFieldErrorSpec("username"));
    }

    @Test
    public void getUserTokenWithNullPassword() {
        userCredentials.setPassword(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.badRequestResponseSpec())
                .spec(AysResponseSpecs.nullCredentialFieldErrorSpec("password"));
    }
}