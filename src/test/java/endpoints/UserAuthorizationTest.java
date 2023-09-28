package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import payload.UserCredentials;
import utility.ConfigurationReader;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class UserAuthorizationTest {
    protected static String accessToken;
    protected static String refreshToken;
    protected static Date tokenGeneratedTime;
    UserCredentials userCredentials = new UserCredentials();

    @BeforeClass
    public void authenticateAndSetTokens() {
        userCredentials.setUsername(ConfigurationReader.getProperty("user4name"));
        userCredentials.setPassword(ConfigurationReader.getProperty("user4password"));
        Response response = given()
                .contentType(ContentType.JSON)
                .body(userCredentials)
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
        String userType = "user";

        if ("ADMIN".equals(userType) || "SUPER_ADMIN".equals(userType)) {
            return "/api/v1/authentication/admin/token";
        }

        return "/api/v1/authentication/token";
    }
}
