package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.TokenInvalidatePayload;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

public class LandingUserInvalidateTokenTest {
    @Test(groups = {"Smoke", "Regression", "User"})
    public void invalidateUserToken() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationUser();
        Response loginResponse = AuthEndpoints.token(loginPayload);
        String accessToken = loginResponse.jsonPath().getString("response.accessToken");
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        TokenInvalidatePayload invalidatePayload = new TokenInvalidatePayload();
        invalidatePayload.setAccessToken(accessToken);
        invalidatePayload.setRefreshToken(refreshToken);
        Response response = AuthEndpoints.invalidateTokens(invalidatePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }
}
