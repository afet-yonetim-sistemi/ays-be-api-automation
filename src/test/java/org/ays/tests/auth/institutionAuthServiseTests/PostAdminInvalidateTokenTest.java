package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Token;
import org.ays.payload.TokenRefreshPayload;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostAdminInvalidateTokenTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void adminInvalidateToken() {
        Token token = Token.generateAdminToken(AdminCredentials.generate());
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(token.getRefreshToken());
        Response response = InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }
}
