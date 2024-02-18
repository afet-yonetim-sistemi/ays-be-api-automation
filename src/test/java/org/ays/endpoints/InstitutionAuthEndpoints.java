package org.ays.endpoints;

import io.restassured.response.Response;
import org.ays.payload.AdminCredentials;
import org.ays.payload.RefreshToken;
import org.openqa.selenium.remote.http.HttpMethod;

public class InstitutionAuthEndpoints {
    public static Response getAdminToken(AdminCredentials adminCredentials) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authAdminToken)
                .body(adminCredentials)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminTokenRefresh(RefreshToken refreshToken) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authAdminTokenRefresh)
                .body(refreshToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response adminInvalidateToken(String accessToken, RefreshToken refreshToken) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.authAdminTokenInvalidate)
                .body(refreshToken)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}
