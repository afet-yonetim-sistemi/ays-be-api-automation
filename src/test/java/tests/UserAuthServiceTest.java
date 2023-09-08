package tests;

import endpoints.UserAuthEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payload.Token;
import payload.UserCredentials;

public class UserAuthServiceTest {
    Token token=new Token();
    UserCredentials userCredentials=new UserCredentials();
    private String refreshToken;
    @Test(priority = 0)
    public void validUserGetToken(){
        userCredentials.setUsername("232180");
        userCredentials.setPassword("367894");
        Response response= UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(200)
                .log().body();
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
    }
    @Test(priority = 1)
    public void invalidUserGetToken(){
        userCredentials.setPassword("3678944");
        Response response=UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .statusCode(401)
                .log().body();

    }
    @Test(priority = 2)
    public void userTokenRefresh(){
        Response response=UserAuthEndpoints.userTokenRefresh(token.getRefreshToken());
        response.then()
                .statusCode(200);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));

    }
    @Test(priority = 3)
    public void userInvalidateToken(){
        Response response=UserAuthEndpoints.userInvalidateToken(token.getAccessToken(), token.getRefreshToken());
        response.then()
                .statusCode(200);
    }
}
