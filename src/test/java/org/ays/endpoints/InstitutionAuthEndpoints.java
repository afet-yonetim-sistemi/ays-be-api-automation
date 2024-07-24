package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.AdminCredentials;
import org.ays.payload.PasswordForgotPayload;
import org.ays.payload.SuperAdminCredentials;
import org.ays.payload.TokenRefreshPayload;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class InstitutionAuthEndpoints {

    public static Response getAdminToken(AdminCredentials adminCredentials) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token")
                .body(adminCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getSuperAdminToken(SuperAdminCredentials superAdminCredentials) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token")
                .body(superAdminCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response adminTokenRefresh(TokenRefreshPayload tokenRefreshPayload) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/refresh")
                .body(tokenRefreshPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminInvalidateToken(String accessToken, TokenRefreshPayload tokenRefreshPayload) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/token/invalidate")
                .body(tokenRefreshPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postPasswordForgot(PasswordForgotPayload passwordForgotPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/password/forgot")
                .body(passwordForgotPayload)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

}
