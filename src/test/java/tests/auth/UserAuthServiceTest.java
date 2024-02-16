package tests.auth;

import endpoints.InstitutionAuthEndpoints;
import endpoints.UserAuthEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.RefreshToken;
import payload.Token;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAuthServiceTest {
    UserCredentials userCredentials;

    @BeforeMethod
    public void setup() {
        userCredentials = Helper.createNewUser();
    }

    @Test()
    public void getTokenForValidUser() {
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.accessToken", notNullValue())
                .body("response.accessTokenExpiresAt", notNullValue())
                .body("response.refreshToken", notNullValue());
    }

    @Test()
    public void getUserTokenWithInvalidPassword() {
        userCredentials.setPassword("wrongPassword");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @Test()
    public void getUserTokenWithInvalidUsername() {
        userCredentials.setUsername("wrongUserName");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));

    }

    @Test()
    public void getUserTokenWithNullUsername() {
        userCredentials.setUsername(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("username"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test()
    public void getUserTokenWithNullPassword() {
        userCredentials.setPassword(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("password"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test()
    public void userTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getUserRefreshToken(userCredentials));
        Response response = UserAuthEndpoints.userTokenRefresh(refreshToken);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.accessToken", notNullValue())
                .body("response.accessTokenExpiresAt", notNullValue())
                .body("response.refreshToken", notNullValue());
    }

    @Test()
    public void userInvalidateToken() {
        Token token = Helper.getUserToken(userCredentials);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        Response response = UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), refreshToken);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }
    @Test()
    public void testUserInvalidRefreshTokenForAccessTokenCreation(){
        Token token = Helper.getUserToken(userCredentials);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), refreshToken);
        Response response=InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));
    }
}
