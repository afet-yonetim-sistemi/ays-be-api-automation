package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.TokenRefreshPayload;
import org.ays.common.util.AysResponseSpecs;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.testng.annotations.Test;

public class PostSuperAdminInvalidateTokenTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void superAdminInvalidateToken() {

        LoginPayload loginPayload = LoginPayload.generateAsSuperAdminUserOne();
        Response loginResponse = InstitutionAuthEndpoints.getAdminToken(loginPayload);
        String accessToken = loginResponse.jsonPath().getString("response.accessToken");
        String refreshToken = loginResponse.jsonPath().getString("response.refreshToken");

        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(refreshToken);
        Response response = InstitutionAuthEndpoints.adminInvalidateToken(accessToken, tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

}
