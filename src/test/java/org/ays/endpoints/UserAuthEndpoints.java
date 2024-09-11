package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.payload.LoginPayload;
import org.ays.payload.TokenRefreshPayload;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class UserAuthEndpoints {

    public static Response getUserToken(LoginPayload userCredentials) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token")
                .body(userCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response userTokenRefresh(TokenRefreshPayload tokenRefreshPayload) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/refresh")
                .body(tokenRefreshPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response userInvalidateToken(String accessToken, TokenRefreshPayload tokenRefreshPayload) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/invalidate")
                .body(tokenRefreshPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}
