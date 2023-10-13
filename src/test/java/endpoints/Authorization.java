package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payload.AdminCredentials;
import payload.UserCredentials;
import utility.ConfigurationReader;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class Authorization {
    protected static String accessToken;
    protected static String refreshToken;
    protected static Date tokenGeneratedTime;
    public static AdminCredentials adminCredentials = new AdminCredentials();
    public static UserCredentials userCredentials = new UserCredentials();

    public static String institutionAuthorization() {
        adminCredentials.setUsername(ConfigurationReader.getProperty("institution1Username"));
        adminCredentials.setPassword(ConfigurationReader.getProperty("institution1Password"));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(adminCredentials)
                .when()
                .post(Routes.base_url + getTokenURL("ADMIN"))
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
        return accessToken;
    }

    public static String userAuthorization(String username, String password) {
        userCredentials.setUsername(username);
        userCredentials.setPassword(password);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(userCredentials)
                .when()
                .post(Routes.base_url + getTokenURL("USER"))
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
        return accessToken;
    }

    private static String getTokenURL(String userType) {
        if ("ADMIN".equals(userType) || "SUPER_ADMIN".equals(userType)) {
            return "/api/v1/authentication/admin/token";
        }
        return "/api/v1/authentication/token";
    }

}
