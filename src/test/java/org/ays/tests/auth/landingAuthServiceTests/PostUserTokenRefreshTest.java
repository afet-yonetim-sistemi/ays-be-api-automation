package org.ays.tests.auth.landingAuthServiceTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.LoginPayload;
import org.ays.payload.Token;
import org.ays.payload.TokenRefreshPayload;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostUserTokenRefreshTest {

    @Test(groups = {"Smoke", "Regression", "User"})
    public void userTokenRefresh() {
        LoginPayload userCredentials = LoginPayload.generate();
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
        LoginPayload userCredentials = LoginPayload.generate();
        Token token = Token.generateUserToken(userCredentials);
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(token.getRefreshToken());
        UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), tokenRefreshPayload);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
