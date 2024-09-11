package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.LoginPayload;
import org.ays.payload.Token;
import org.ays.payload.TokenRefreshPayload;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostSuperAdminInvalidateTokenTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void superAdminInvalidateToken() {
        Token token = Token.generateSuperAdminToken(LoginPayload.generate());
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(token.getRefreshToken());
        Response response = InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

}
