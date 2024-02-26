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

    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
    }

    @Test
    public void userTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getUserRefreshToken(userCredentials));
        Response response = UserAuthEndpoints.userTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec())
                .spec(AysResponseSpecs.getTokenResponseSpec());
    }

    @Test
    public void testUserInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Helper.getUserToken(userCredentials);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());
    }
}
