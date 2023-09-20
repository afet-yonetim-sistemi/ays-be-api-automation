package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import payload.AdminCredentials;
import utility.ConfigurationReader;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class AuthorizationTest {
    protected static String accessToken;
    protected static String refreshToken;
    protected static Date tokenGeneratedTime;
    AdminCredentials adminCredentials=new AdminCredentials();

    @BeforeClass
    public void authenticateAndSetTokens() {
        adminCredentials.setUsername(ConfigurationReader.getProperty("institution1Username"));
        adminCredentials.setPassword(ConfigurationReader.getProperty("institution1Password"));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(adminCredentials)
                .when()
                .post(Routes.base_url + getTokenURL())
                .then()
                .extract()
                .response();

        if (response.jsonPath().getBoolean("isSuccess")) {
            accessToken = response.jsonPath().getString("response.accessToken");
            refreshToken = response.jsonPath().getString("response.refreshToken");
            tokenGeneratedTime = new Date();
        } else {
            System.out.println(response.jsonPath().prettify());
        }
    }

    private String getTokenURL() {
        String userType = "ADMIN";

        if ("ADMIN".equals(userType) || "SUPER_ADMIN".equals(userType)) {
            return "/api/v1/authentication/admin/token";
        }

        return "/api/v1/authentication/token";
    }
}
