package tests;

import endpoints.InstitutionAuthEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payload.AdminCredentials;
import payload.Token;
import utility.ConfigurationReader;

public class InstitutionAuthServiceTest {
    Token token = new Token();
    AdminCredentials adminCredentials = new AdminCredentials();

    @Test(priority = 0)
    public void getTokenForValidAdmin() {
        adminCredentials.setUsername(ConfigurationReader.getProperty("institution1Username"));
        adminCredentials.setPassword(ConfigurationReader.getProperty("institution1Password"));
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .log().body()
                .statusCode(200);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));

    }

    @Test(priority = 1)
    public void getTokenForInvalidAdmin() {
        adminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .log().body()
                .statusCode(401);
    }

    @Test(priority = 2)
    public void adminTokenRefresh() {
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(token.getRefreshToken());
        response
                .then()
                .log().body()
                .statusCode(200);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
    }

    @Test(priority = 3)
    public void adminInvalidateToken() {
        Response response=InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(),token.getRefreshToken());
        response
                .then()
                .log().body()
                .statusCode(200);
    }

}
