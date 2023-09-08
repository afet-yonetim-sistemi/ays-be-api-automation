package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payload.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserAuthEndpoints {
    public static Response getUserToken(UserCredentials userCredentials){
        return given()
                .contentType(ContentType.JSON)
                .body(userCredentials)
                .when()
                .post(Routes.authUserToken);
    }
    public static Response userTokenRefresh(String refreshToken){
        return given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"refreshToken\": \""+refreshToken+"\"\n" +
                        "}")
                .when()
                .post(Routes.authUserTokenRefresh);

    }
    public static Response userInvalidateToken(String accessToken,String refreshToken){
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
