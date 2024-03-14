package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.AdminCredentials;
import org.ays.payload.TokenRefreshPayload;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class InstitutionAuthEndpoints {

    public static Response getAdminToken(AdminCredentials adminCredentials) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/admin/token")
                .body(adminCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminTokenRefresh(TokenRefreshPayload tokenRefreshPayload) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/admin/token/refresh")
                .body(tokenRefreshPayload)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminInvalidateToken(String accessToken, TokenRefreshPayload tokenRefreshPayload) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/authentication/admin/token/invalidate")
                .body(tokenRefreshPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}
