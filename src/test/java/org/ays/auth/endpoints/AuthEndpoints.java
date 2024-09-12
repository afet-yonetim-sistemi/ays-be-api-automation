package org.ays.auth.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.PasswordForgotPayload;
import org.ays.auth.payload.TokenInvalidatePayload;
import org.ays.auth.payload.TokenRefreshPayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class AuthEndpoints {

    public static Response token(LoginPayload loginPayload) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token")
                .body(loginPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response refreshAccessToken(TokenRefreshPayload tokenRefreshPayload) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/refresh")
                .body(tokenRefreshPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response invalidateTokens(TokenInvalidatePayload tokenInvalidatePayload) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/invalidate")
                .body(Map.of("refreshToken", tokenInvalidatePayload.getRefreshToken()))
                .token(tokenInvalidatePayload.getAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response forgotPassword(PasswordForgotPayload passwordForgotPayload) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/password/forgot")
                .body(passwordForgotPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

}
