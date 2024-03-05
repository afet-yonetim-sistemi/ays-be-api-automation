package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.RefreshToken;
import org.ays.payload.Token;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostAdminInvalidateTokenTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void adminInvalidateToken() {
        Token token = Helper.getAdminToken(Helper.setIntsAdminCredentials());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        Response response = InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), refreshToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }
}
