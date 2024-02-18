package org.ays.endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.AdminCredentials;
import org.ays.payload.SuperAdminCredentials;
import org.ays.payload.UserCredentials;
import org.ays.utility.ConfigurationReader;

@UtilityClass
public class Authorization {

    public static String institutionAuthorization() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setUsername(ConfigurationReader.getProperty("institution1Username"));
        adminCredentials.setPassword(ConfigurationReader.getProperty("institution1Password"));

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(adminCredentials)
                .when()
                .post("/api/v1/authentication/admin/token")
                .then()
                .extract()
                .response();

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String userAuthorization(String username, String password) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(username);
        userCredentials.setPassword(password);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userCredentials)
                .when()
                .post("/api/v1/authentication/token")
                .then()
                .extract()
                .response();

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String superAdminAuthorization() {
        SuperAdminCredentials superAdminCredentials = new SuperAdminCredentials();
        superAdminCredentials.setUsername(ConfigurationReader.getProperty("superAdmin_username"));
        superAdminCredentials.setPassword(ConfigurationReader.getProperty("superAdmin_password"));

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(superAdminCredentials)
                .when()
                .post("/api/v1/authentication/admin/token")
                .then()
                .extract()
                .response();

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

}
