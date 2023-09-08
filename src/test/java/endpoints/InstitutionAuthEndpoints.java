package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payload.AdminCredentials;

import static io.restassured.RestAssured.given;

public class InstitutionAuthEndpoints {
    public static Response getAdminToken(AdminCredentials adminCredentials){
        return given()
                .contentType(ContentType.JSON)
                .body(adminCredentials)
                .when()
                .post(Routes.authAdminToken);
    }


    public static Response adminTokenRefresh(String refreshToken ){
        return given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"refreshToken\": \""+refreshToken+"\"\n" +
                        "}")
                .when()
                .post(Routes.authAdminTokenRefresh);
    }


    public static Response adminInvalidateToken(String accessToken,String refreshToken){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"refreshToken\": \""+refreshToken+"\"\n" +
                        "}")
                .when()
                .post(Routes.authUserTokenInvalidate);
    }

}
