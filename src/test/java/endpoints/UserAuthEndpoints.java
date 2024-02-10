package endpoints;

import io.restassured.response.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import payload.RefreshToken;
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

    public static Response userTokenRefresh(RefreshToken refreshToken) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authUserTokenRefresh)
                .body(refreshToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response userInvalidateToken(String accessToken, RefreshToken refreshToken) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authUserTokenInvalidate)
                .body(refreshToken)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    private record RefreshTokenRequest(String refreshToken) {
    }

    private record AccessTokenRequest(String accessToken) {
    }

}
