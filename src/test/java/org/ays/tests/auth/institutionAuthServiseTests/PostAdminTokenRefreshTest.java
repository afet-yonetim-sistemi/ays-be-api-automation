package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.RefreshToken;
import org.ays.payload.Token;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostAdminTokenRefreshTest {
    @Test
    public void adminTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getAdminRefreshToken(Helper.setIntsAdminCredentials()));
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec())
                .spec(AysResponseSpecs.getTokenResponseSpec());
    }
    @Test
    public void testAdminInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Helper.getAdminToken(Helper.setIntsAdminCredentials());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());
    }
}
