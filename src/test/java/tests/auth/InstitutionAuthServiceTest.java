package tests.auth;

import endpoints.InstitutionAuthEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.AdminCredentials;
import payload.Token;
import utility.ConfigurationReader;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class InstitutionAuthServiceTest {
    Token token = new Token();
    AdminCredentials adminCredentials = new AdminCredentials();
    public Logger logger;
    @BeforeClass
    public void setup(){
        logger= LogManager.getLogger(this.getClass());
    }


    @Test(priority = 0)
    public void getTokenForValidAdmin() {
        logger.info("Test case IAS_01 is running");
        adminCredentials.setUsername(ConfigurationReader.getProperty("institution1Username"));
        adminCredentials.setPassword(ConfigurationReader.getProperty("institution1Password"));
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
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));


    }

    @Test(priority = 1)
    public void getTokenForInvalidAdmin() {
        logger.info("Test case IAS_02 is running");
        adminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time",notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header",equalTo("AUTH ERROR"))
                .body("isSuccess",equalTo(false));
    }

    @Test(priority = 2)
    public void adminTokenRefresh() {
        logger.info("Test case IAS_05 is running");
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(token.getRefreshToken());
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
    public void adminInvalidateToken() {
        logger.info("Test case IAS_06 is running");
        Response response=InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(),token.getRefreshToken());
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

}
