package endpoints;

import io.restassured.response.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import payload.UserCredentials;

public class UserAuthEndpoints {

    public static Response getUserToken(UserCredentials userCredentials) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authUserToken)
                .body(userCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response userTokenRefresh(String refreshToken) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authUserTokenRefresh)
                .body(new RefreshTokenRequest(refreshToken))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response userInvalidateToken(String accessToken, String refreshToken) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authUserTokenInvalidate)
                .body("{\n" +
                        "    \"refreshToken\": \"" + refreshToken + "\"\n" +
                        "}")
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    private record RefreshTokenRequest(String refreshToken) {
    }

    private record AccessTokenRequest(String accessToken) {
    }

}
