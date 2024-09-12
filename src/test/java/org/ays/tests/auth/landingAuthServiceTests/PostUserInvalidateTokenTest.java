package org.ays.tests.auth.landingAuthServiceTests;

import io.restassured.response.Response;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.TokenRefreshPayload;
import org.ays.common.util.AysResponseSpecs;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.UserAuthEndpoints;
import org.testng.annotations.Test;

public class PostUserInvalidateTokenTest {
    @Test(groups = {"Smoke", "Regression", "User"})
    public void userInvalidateToken() {

        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        Response loginResponse = InstitutionAuthEndpoints.getAdminToken(loginPayload);
        String accessToken = loginResponse.jsonPath().getString("response.accessToken");
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = UserAuthEndpoints.userInvalidateToken(accessToken, tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }
}
