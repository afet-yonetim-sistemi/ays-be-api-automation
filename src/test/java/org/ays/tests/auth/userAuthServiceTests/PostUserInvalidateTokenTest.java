package org.ays.tests.auth.userAuthServiceTests;

import io.restassured.response.Response;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.RefreshToken;
import org.ays.payload.Token;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PostUserInvalidateTokenTest {
    UserCredentials userCredentials;

    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
    }

    @Test
    public void userInvalidateToken() {
        Token token = Helper.getUserToken(userCredentials);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        Response response = UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), refreshToken);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec());
    }
}
