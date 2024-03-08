package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Helper;
import org.ays.payload.RefreshToken;
import org.ays.payload.Token;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostAdminTokenRefreshTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void adminTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getAdminRefreshToken(AdminCredentials.generateIntsAdminCredentials()));
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }
    @Test(groups = {"Regression", "Institution"})
    public void testAdminInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Token.generateAdminToken(AdminCredentials.generateIntsAdminCredentials());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
