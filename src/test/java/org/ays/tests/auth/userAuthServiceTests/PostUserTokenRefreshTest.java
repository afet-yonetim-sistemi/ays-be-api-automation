package org.ays.tests.auth.userAuthServiceTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.RefreshToken;
import org.ays.payload.Token;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PostUserTokenRefreshTest {
    UserCredentials userCredentials;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = UserCredentials.generateCreate();
    }

    @Test(groups = {"Smoke", "Regression", "User"})
    public void userTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getUserRefreshToken(userCredentials));
        Response response = UserAuthEndpoints.userTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void testUserInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Token.generateUserToken(userCredentials);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
