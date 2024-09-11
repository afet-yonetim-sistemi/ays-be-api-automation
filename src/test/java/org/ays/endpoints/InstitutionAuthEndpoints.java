package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.payload.PasswordForgotPayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.payload.LoginPayload;
import org.ays.payload.TokenRefreshPayload;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class InstitutionAuthEndpoints {

    public static Response getAdminToken(LoginPayload adminCredentials) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token")
                .body(adminCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getSuperAdminToken(LoginPayload superAdminCredentials) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token")
                .body(superAdminCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response adminTokenRefresh(TokenRefreshPayload tokenRefreshPayload) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/refresh")
                .body(tokenRefreshPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminInvalidateToken(String accessToken, TokenRefreshPayload tokenRefreshPayload) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/invalidate")
                .body(tokenRefreshPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postPasswordForgot(PasswordForgotPayload passwordForgotPayload) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/password/forgot")
                .body(passwordForgotPayload)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

}
