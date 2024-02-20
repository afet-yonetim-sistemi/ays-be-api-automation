package org.ays.tests.auth;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.RefreshToken;
import org.ays.payload.Token;
import org.ays.payload.UserCredentials;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.ays.utility.AysResponseSpecs;

public class UserAuthServiceTest {
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

    @Test
    public void userTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getUserRefreshToken(userCredentials));
        Response response = UserAuthEndpoints.userTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec())
                .spec(AysResponseSpecs.getTokenResponseSpec());
    }

    @Test
    public void userInvalidateToken() {
        Token token = Helper.getUserToken(userCredentials);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        Response response = UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), refreshToken);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec());
    }

    @Test
    public void testUserInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Helper.getUserToken(userCredentials);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());
    }
}
