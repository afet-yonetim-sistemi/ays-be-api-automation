package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class AuthorizationTest {
    protected static String accessToken;
    protected static String refreshToken;
    protected static Date tokenGeneratedTime;

    @BeforeClass
    public void authenticateAndSetTokens() {
        String requestBody = String.format("{\"username\": \"ays-admin-2\", \"password\": \"A123y456S.\"}");

        /**authentication request*/
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Routes.base_url + getTokenURL())
                .then()
                .log().body()
                .extract()
                .response();

        /**Verify credentials*/
        if (response.jsonPath().getBoolean("isSuccess")) {
            accessToken = response.jsonPath().getString("response.accessToken");
            refreshToken = response.jsonPath().getString("response.refreshToken");
            tokenGeneratedTime = new Date();
        } else {
            System.out.println(response.jsonPath().prettify());
        }
    }

    private String getTokenURL() {
        /**I need to optimize this part in oder to send different userType from different environment */
        String userType = "ADMIN";

        if ("ADMIN".equals(userType) || "SUPER_ADMIN".equals(userType)) {
            return "/api/v1/authentication/admin/token";
        }

        return "/api/v1/authentication/token";
    }
}
