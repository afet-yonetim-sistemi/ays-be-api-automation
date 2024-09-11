package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.LoginPayload;
import org.ays.payload.Token;
import org.ays.payload.TokenRefreshPayload;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostAdminTokenRefreshTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void adminTokenRefresh() {
        LoginPayload adminCredentials = LoginPayload.generateAsAdminUserOne();
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        Response loginResponse = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        String  refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }
    @Test(groups = {"Regression", "Institution"})
    public void testAdminInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Token.generateAdminToken(LoginPayload.generateAsAdminUserOne());
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(token.getRefreshToken());
        InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), tokenRefreshPayload);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
