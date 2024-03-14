package org.ays.tests.auth.userAuthServiceTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.Token;
import org.ays.payload.TokenRefreshPayload;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PostUserTokenRefreshTest {
    UserCredentials userCredentials;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = InstitutionEndpoints.generateANewUser();
    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void userTokenRefresh() {
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        Response loginResponse = UserAuthEndpoints.getUserToken(userCredentials);
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = UserAuthEndpoints.userTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void testUserInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Token.generateUserToken(userCredentials);
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(token.getRefreshToken());
        UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), tokenRefreshPayload);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
