package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.TokenRefreshPayload;
import org.ays.payload.UserCredentials;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class UserAuthEndpoints {

    public static Response getUserToken(UserCredentials userCredentials) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token")
                .body(userCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response userTokenRefresh(TokenRefreshPayload tokenRefreshPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/refresh")
                .body(tokenRefreshPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response userInvalidateToken(String accessToken, TokenRefreshPayload tokenRefreshPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/invalidate")
                .body(tokenRefreshPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}
