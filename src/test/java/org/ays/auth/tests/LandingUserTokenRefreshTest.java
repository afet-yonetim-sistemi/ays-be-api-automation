package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.TokenInvalidatePayload;
import org.ays.auth.payload.TokenRefreshPayload;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

public class LandingUserTokenRefreshTest {

    @Test(groups = {"Smoke", "Regression", "User"})
    public void userTokenRefresh() {

        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        Response loginResponse = AuthEndpoints.token(loginPayload);
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = AuthEndpoints.refreshAccessToken(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void testUserInvalidRefreshTokenForAccessTokenCreation() {

        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        Response loginResponse = AuthEndpoints.token(loginPayload);
        String accessToken = loginResponse.jsonPath().getString("response.accessToken");
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        TokenInvalidatePayload invalidatePayload = new TokenInvalidatePayload();
        invalidatePayload.setAccessToken(accessToken);
        invalidatePayload.setRefreshToken(refreshToken);
        AuthEndpoints.invalidateTokens(invalidatePayload);

        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = AuthEndpoints.refreshAccessToken(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
