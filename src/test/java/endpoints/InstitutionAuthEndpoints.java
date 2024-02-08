package endpoints;

import io.restassured.response.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import payload.AdminCredentials;

public class InstitutionAuthEndpoints {
    public static Response getAdminToken(AdminCredentials adminCredentials) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authAdminToken)
                .body(adminCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminTokenRefresh(String refreshToken) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authAdminTokenRefresh)
                .body("{\n" +
                        "    \"refreshToken\": \"" + refreshToken + "\"\n" +
                        "}")
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminInvalidateToken(String accessToken, String refreshToken) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authAdminTokenInvalidate)
                .body("{\n" +
                        "    \"refreshToken\": \"" + refreshToken + "\"\n" +
                        "}")
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}
