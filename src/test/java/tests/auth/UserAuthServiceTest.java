package tests.auth;

import endpoints.UserAuthEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payload.Token;
import payload.UserCredentials;
import utility.ConfigurationReader;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAuthServiceTest {
    Token token=new Token();
    UserCredentials userCredentials=new UserCredentials();
    @Test(priority = 0)
    public void getTokenForValidUser(){
        userCredentials.setUsername(ConfigurationReader.getProperty("user1name"));
        userCredentials.setPassword(ConfigurationReader.getProperty("user1password"));
        Response response= UserAuthEndpoints.getUserToken(userCredentials);
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
    @Test(priority = 1)
    public void getTokenInvalidUser(){
        userCredentials.setPassword("3678944");
        Response response=UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time",notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header",equalTo("AUTH ERROR"))
                .body("isSuccess",equalTo(false));

    }
    @Test(priority = 2)
    public void userTokenRefresh(){
        Response response=UserAuthEndpoints.userTokenRefresh(token.getRefreshToken());
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
    public void userInvalidateToken(){
        Response response=UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), token.getRefreshToken());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }
}
