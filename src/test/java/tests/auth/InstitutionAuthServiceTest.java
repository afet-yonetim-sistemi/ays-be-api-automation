package tests.auth;

import endpoints.InstitutionAuthEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.AdminCredentials;
import payload.Helper;
import payload.RefreshToken;
import payload.Token;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class InstitutionAuthServiceTest {
    AdminCredentials adminCredentials;

    @BeforeMethod
    public void setup() {
        adminCredentials = new AdminCredentials();
    }


    @Test()
    public void getTokenForValidAdmin() {
        adminCredentials = Helper.setIntsAdminCredentials();
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
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
    public void getAdminTokenWithInvalidUsername() {
        adminCredentials.setUsername("invalidUsername");
        adminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @Test()
    public void adminTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getAdminRefreshToken(Helper.setIntsAdminCredentials()));
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
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
    public void adminInvalidateToken() {
        Token token = Helper.getAdminToken(Helper.setIntsAdminCredentials());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        Response response = InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), refreshToken);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test()
    public void testAdminInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Helper.getAdminToken(Helper.setIntsAdminCredentials());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));
    }
}
