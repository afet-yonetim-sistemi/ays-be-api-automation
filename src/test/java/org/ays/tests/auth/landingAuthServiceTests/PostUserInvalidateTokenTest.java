package org.ays.tests.auth.landingAuthServiceTests;

import io.restassured.response.Response;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.TokenRefreshPayload;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.Token;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PostUserInvalidateTokenTest {
    @Test(groups = {"Smoke", "Regression", "User"})
    public void userInvalidateToken() {
        LoginPayload userCredentials = LoginPayload.generateAsUserOne();
        Token token = Token.generateUserToken(userCredentials);
        TokenRefreshPayload tokenRefreshPayload = new TokenRefreshPayload();
        tokenRefreshPayload.setRefreshToken(token.getRefreshToken());
        Response response = UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), tokenRefreshPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }
}
