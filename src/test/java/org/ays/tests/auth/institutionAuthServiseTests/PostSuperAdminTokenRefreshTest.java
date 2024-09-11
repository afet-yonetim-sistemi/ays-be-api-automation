package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.auth.payload.LoginPayload;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.Token;
import org.ays.payload.TokenRefreshPayload;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostSuperAdminTokenRefreshTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void superAdminTokenRefresh() {
        LoginPayload superAdminCredentials = LoginPayload.generateAsSuperAdminUserOne();
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        Response loginResponse = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void testSuperAdminInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Token.generateSuperAdminToken(LoginPayload.generateAsSuperAdminUserOne());
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(token.getRefreshToken());
        InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), tokenRefreshPayload);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

}
