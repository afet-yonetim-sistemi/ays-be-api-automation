package tests.auth;

import endpoints.UserAuthEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Helper;
import payload.Token;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAuthServiceTest {
    Token token = new Token();
    UserCredentials userCredentials = new UserCredentials();
    private String username;
    private String password;

    @BeforeClass
    public void setup() {
        userCredentials = Helper.createNewUser();
        username = userCredentials.getUsername();
        password = userCredentials.getPassword();
    }

    @Test(priority = 0)
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
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
    }

    @Test(priority = 4)
    public void getTokenInvalidPassword() {
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

    @Test(priority = 5)
    public void getTokenInvalidUsername() {
        userCredentials.setPassword(password);
        userCredentials.setUsername("invalidUserName");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));

    }

    @Test(priority = 6)
    public void getTokenMissingUsername() {
        userCredentials.setPassword(password);
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

    @Test(priority = 7)
    public void getTokenMissingPassword() {
        userCredentials.setUsername(username);
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

    @Test(priority = 2)
    public void userTokenRefresh() {
        Response response = UserAuthEndpoints.userTokenRefresh(token.getRefreshToken());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.accessToken", notNullValue())
                .body("response.accessTokenExpiresAt", notNullValue())
                .body("response.refreshToken", notNullValue());
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));

    }

    @Test(priority = 3)
    public void userInvalidateToken() {
        Response response = UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), token.getRefreshToken());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }
}
