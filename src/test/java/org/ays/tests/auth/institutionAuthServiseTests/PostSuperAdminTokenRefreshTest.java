package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.TokenRefreshPayload;
import org.ays.common.util.AysResponseSpecs;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.testng.annotations.Test;

public class PostSuperAdminTokenRefreshTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void superAdminTokenRefresh() {

        LoginPayload loginPayload = LoginPayload.generateAsSuperAdminUserOne();
        Response loginResponse = InstitutionAuthEndpoints.getAdminToken(loginPayload);
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void testSuperAdminInvalidRefreshTokenForAccessTokenCreation() {

        LoginPayload loginPayload = LoginPayload.generateAsSuperAdminUserOne();
        Response loginResponse = InstitutionAuthEndpoints.getAdminToken(loginPayload);
        String accessToken = loginResponse.jsonPath().getString("response.accessToken");
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(refreshToken);
        InstitutionAuthEndpoints.adminInvalidateToken(accessToken, tokenRefreshPayload);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

}
